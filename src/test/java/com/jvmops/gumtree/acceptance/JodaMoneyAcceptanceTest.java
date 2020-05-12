package com.jvmops.gumtree.acceptance;

import com.jvmops.gumtree.ScrapperConfig;
import com.jvmops.gumtree.scraper.PriceChange;
import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JodaMoneyAcceptanceTest {

    public static final LocalDate STUB = LocalDate.now();
    public static final Money OLD_PRICE = Money.of(ScrapperConfig.DEFAULT_CURRENCY, new BigDecimal("2000"));
    public static final Money NEW_PRICE = Money.of(ScrapperConfig.DEFAULT_CURRENCY, new BigDecimal("1500"));

    @Test
    void price_went_down() {
        var priceChange = new PriceChange(STUB, OLD_PRICE, NEW_PRICE);
        Assertions.assertTrue(priceChange.priceWentDown());
    }
}
