package com.example.vt.bookstore.service;

import com.example.vt.bookstore.dto.Book;
import com.example.vt.bookstore.util.ThreadUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Profile("WONDER")
public class WonderBookCollectionService implements BookCollectionService {
    @Value("${book.store.name}")
    private String storeName;
    private List<Book> books;

    @PostConstruct
    public void init() {
        books = List.of(
                new Book(
                        storeName,
                        "And Then There Were None",
                        "Agatha Christie",
                        10,
                        300,
                        bookUrl("And Then There Were None")
                ),
                new Book(
                        storeName,
                        "A Study In Scarlet",
                        "Arthur Conan Doyle",
                        10,
                        108,
                        bookUrl("A Study In Scarlet")
                ),
                new Book(
                        storeName,
                        "The Day Of The Jackal",
                        "Fredrick Forsyth",
                        11,
                        464,
                        bookUrl("The Day Of The Jackal")
                ),
                new Book(
                        storeName,
                        "The Wisdom Of Father Brown",
                        "G.K. Chesterton",
                        7,
                        136,
                        bookUrl("The Wisdom Of Father Brown")
                ),
                new Book(
                        storeName,
                        "The Poet",
                        "Michael Connelly",
                        15,
                        528,
                        bookUrl("The Poet")
                )
        );
    }

    private String bookUrl(String title) {
        try {
            return String.format(
                    "http://wonder:8081/store/book?name=%s",
                    URLEncoder.encode(title, StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return "?";
        }
    }

    @Override
    public Book findBook(String name) {
        ThreadUtil.sleep(5);
        return books.stream()
                .filter(book -> book.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();
    }
}
