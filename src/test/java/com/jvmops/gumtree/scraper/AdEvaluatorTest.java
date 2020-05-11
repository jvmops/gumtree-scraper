package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Main.class)
class AdEvaluatorTest extends ScrapperDataInitializer {

    @Autowired
    private AdEvaluator adEvaluator;

    @BeforeEach
    public void setup() {
        reloadApartments();
    }

    @Test
    public void if_city_is_null_or_empty_exception_will_be_thrown() {
        ScrappedAd scrappedAd = scrappedAd("", "Just added apartment");
        assertThrows(
                IllegalArgumentException.class,
                () -> adEvaluator.processAd(scrappedAd)
        );
    }

    @Test
    public void non_existing_ad_will_be_saved() {
        // in the db there is only one ad from Katowice
        ScrappedAd scrappedAd = scrappedAd("Wroclaw", "Just added apartment");
        adEvaluator.processAd(scrappedAd);

        ScrappedAd fromDb = adEvaluator.findInRepository(scrappedAd)
                .orElse(scrappedAd);
        assertEquals("Wroclaw", fromDb.getCity());
    }

    @Test
    public void modification_date_of_refreshed_ad_will_be_updated() {
        ScrappedAd scrappedAd = scrappedAd("Katowice", "Modify this ad");
        adEvaluator.processAd(scrappedAd);

        ScrappedAd fromDb = adEvaluator.findInRepository(scrappedAd)
                .orElseThrow(() -> new IllegalStateException("Scrapped ad not found in the repository"));
        assertEquals(LocalDate.now(clock), fromDb.getGumtreeModificationDate(), "Date has not been updated");
        assertEquals(3, fromDb.getUpdates().size());
    }
}
