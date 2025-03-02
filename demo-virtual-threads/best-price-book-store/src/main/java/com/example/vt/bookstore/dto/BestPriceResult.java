package com.example.vt.bookstore.dto;

import java.util.List;

public record BestPriceResult(Book bestPriceDeal,
                              List<Book> allDeals,
                              RestCallTimeStatistics restCallTimeStatistics) {
}
