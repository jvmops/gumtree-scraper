package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ScrappedAd;
import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.jvmops.gumtree.ScrapperConfig.DEFAULT_CURRENCY;

public class AdEvaluatorUnitTest {
    private static final ScrappedAd OLDER = ScrappedAd.builder()
            .gumtreeId("1")
            .price(Money.of(DEFAULT_CURRENCY, new BigDecimal("500")))
            .build();
    private static final ScrappedAd NEWER = ScrappedAd.builder()
            .gumtreeId("2")
            .price(Money.of(DEFAULT_CURRENCY, new BigDecimal("600")))
            .build();
    private static final ScrappedAd THE_SAME = ScrappedAd.builder()
            .gumtreeId("2")
            .price(Money.of(DEFAULT_CURRENCY, new BigDecimal("600")))
            .build();

    @Test
    void gumtree_id_has_changed() {
        Assertions.assertTrue(
                AdEvaluator.GUMTREE_ID_HAS_CHANGED.test(OLDER, NEWER)
        );
    }

    @Test
    void gumtree_id_has_not_changed() {
        Assertions.assertFalse(
                AdEvaluator.GUMTREE_ID_HAS_CHANGED.test(NEWER, THE_SAME)
        );
    }

    @Test
    void price_has_changed() {
        Assertions.assertTrue(
                AdEvaluator.PRICE_HAS_CHANGED.test(OLDER, NEWER)
        );
    }
}
