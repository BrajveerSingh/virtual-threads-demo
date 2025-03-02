package com.example.vt.bookstore.controller;

import com.example.vt.bookstore.dto.Book;
import com.example.vt.bookstore.service.BookCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class BookStoreController {
    @Autowired
    private BookCollectionService bookCollectionService;

    @GetMapping("/v1/book")
    public ResponseEntity<Book> getBook(@RequestParam final String name) {
        return ResponseEntity.ok(bookCollectionService.findBook(name));
    }
}
