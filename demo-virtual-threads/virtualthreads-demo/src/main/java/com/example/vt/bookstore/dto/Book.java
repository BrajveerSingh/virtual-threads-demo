package com.example.vt.bookstore.dto;

public record Book(String bookStore,
                   String name,
                   String author,
                   double cost,
                   int numberOfPages,
                   String link) {
}
