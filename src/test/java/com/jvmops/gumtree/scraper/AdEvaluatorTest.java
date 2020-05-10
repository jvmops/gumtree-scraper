package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Main.class)
class AdEvaluatorTest extends DataInitializer {

    @Autowired
    private AdEvaluator adEvaluator;

    @BeforeEach
    public void setup() {
        reloadApartments();
    }

    @Test
    public void if_city_is_null_or_empty_exception_will_be_thrown() {
        Ad scrappedAd = scrappedAd("", "Just added apartment");
        assertThrows(
                IllegalArgumentException.class,
                () -> adEvaluator.processAd(scrappedAd)
        );
    }

    @Test
    public void non_existing_ad_will_be_saved() {
        // in the db there is only one ad from Katowice
        Ad scrappedAd = scrappedAd("Wroclaw", "Just added apartment");
        adEvaluator.processAd(scrappedAd);

        Ad fromDb = adEvaluator.findInRepository(scrappedAd)
                .orElse(scrappedAd);
        assertEquals("Wroclaw", fromDb.getCity());
    }

    @Test
    public void creation_date_of_refreshed_ad_will_be_updated() {
        Ad scrappedAd = scrappedAd("Katowice", "Modify this ad");
        adEvaluator.processAd(scrappedAd);

        Ad fromDb = adEvaluator.findInRepository(scrappedAd)
                .orElseThrow(() -> new IllegalStateException("Scrapped ad not found in the repository"));
        assertEquals(time.now().toLocalDate(), fromDb.getGumtreeCreationDate(), "Date has not been updated");
        assertEquals(3, fromDb.getUpdates().size());
    }
}
