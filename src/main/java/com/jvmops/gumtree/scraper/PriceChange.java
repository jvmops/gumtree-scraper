package com.jvmops.gumtree.scraper;

import org.joda.money.Money;

import java.time.LocalDate;

public record PriceChange(LocalDate theDay, Money oldPrice, Money newPrice) {
    public boolean priceWentDown() {
        return oldPrice.compareTo(newPrice) > 0;
    }
}
