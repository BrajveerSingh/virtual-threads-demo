package com.example.vt.bookstore.service;

import com.example.vt.bookstore.dto.BestPriceResult;
import com.example.vt.bookstore.dto.Book;
import com.example.vt.bookstore.dto.RestCallTimeStatistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.StructuredTaskScope;

@Service
public class BookRetrievalService {
    public static final ScopedValue<RestCallTimeStatistics> STATISTICS_SCOPED_VALUE = ScopedValue.newInstance();
    @Value("#{${book.store.base.urls}}")
    private Map<String, String> urlsByStoreName;

    private final RestClient restClient = RestClient.create();

    public List<Book> getBookFromAllStores(final String bookName){
        try(var scope = new StructuredTaskScope<Book>()){
            List<StructuredTaskScope.Subtask<Book>> bookSubtasks = new ArrayList<>();
            urlsByStoreName.forEach(
                    (name,url) -> {
                        bookSubtasks.add(scope.fork(()-> getBookFromStore(name, url, bookName)));
                    }
            );
            scope.join();
            //Dump all stacktrace for failures
            bookSubtasks.stream()
                    .filter(task -> task.state() == StructuredTaskScope.Subtask.State.FAILED)
                    .map(StructuredTaskScope.Subtask::exception)
                    .forEach(Throwable::printStackTrace);

            return bookSubtasks.stream()
                    .filter(task -> task.state() == StructuredTaskScope.Subtask.State.SUCCESS)
                    .map(StructuredTaskScope.Subtask::get)
                    .toList();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Book getBookFromStore(String storeName, String url, String bookName) {
        long start = System.currentTimeMillis();
        Book book = restClient.get()
                .uri(url + "/store/v1/book", t -> t.queryParam("name", bookName).build())
                .retrieve()
                .body(Book.class);
        long end = System.currentTimeMillis();
        RestCallTimeStatistics statistics = STATISTICS_SCOPED_VALUE.get();
        statistics.addStatistic(storeName, (end - start));
        return book;
    }

    public BestPriceResult getBestDealForBook(String name) {

        long start = System.currentTimeMillis();
        RestCallTimeStatistics restCallTimeStatistics = new RestCallTimeStatistics();
        try {
            List<Book> bookFromAllStores = ScopedValue.callWhere(STATISTICS_SCOPED_VALUE, restCallTimeStatistics, () -> getBookFromAllStores(name));
            Book bestPricedBook = bookFromAllStores.stream()
                    .min(Comparator.comparing(Book::cost))
                    .orElseThrow();
            return new BestPriceResult(bestPricedBook, bookFromAllStores, restCallTimeStatistics);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            long end = System.currentTimeMillis();
            restCallTimeStatistics.addStatistic("Best Price Store", (end-start));
            restCallTimeStatistics.dumpStatistics();
        }
    }
}
