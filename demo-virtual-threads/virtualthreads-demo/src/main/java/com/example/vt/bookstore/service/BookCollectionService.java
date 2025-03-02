package com.example.vt.bookstore.service;

import com.example.vt.bookstore.dto.Book;

public interface BookCollectionService {
    Book findBook(String name);
}
