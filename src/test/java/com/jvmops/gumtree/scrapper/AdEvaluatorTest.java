package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.AdCollectionTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AdEvaluatorTest extends AdCollectionTest {

    @Autowired
    private AdEvaluator adEvaluator;

    @Test
    public void creation_date_of_refreshed_ad_will_be_updated() {
        Ad scrappedAd = Ad.builder()
                .title("Modify this ad")
                .gumtreeCreationDate(DATE_NOW)
                .build();

        adEvaluator.processAds(Stream.of(scrappedAd));

        Ad fromDb = adRepository.findByTitle("Modify this ad");
        assertEquals(3, fromDb.getUpdates().size());
    }
}
