package com.example.vt.bookstore.controller;

import com.example.vt.bookstore.dto.BestPriceResult;
import com.example.vt.bookstore.service.BookRetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/virtualstore")
public class BestPriceBookController {

    @Autowired
    private BookRetrievalService bookRetrievalService;

    @GetMapping("/v1/book")
    public ResponseEntity<BestPriceResult> getBestPriceForBook(@RequestParam final String name) {
        try {
            return ResponseEntity.ok(bookRetrievalService.getBestDealForBook(name));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
