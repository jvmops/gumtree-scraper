package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
class AdEvaluatorTest extends DataInitializer {

    @Autowired
    private AdEvaluator adEvaluator;

    @Test
    public void if_city_is_null_or_empty_exception_will_be_thrown() {
        Ad scrappedAd = scrappedAd(null);
        assertThrows(
                IllegalArgumentException.class,
                () -> adEvaluator.processAds(List.of(scrappedAd))
        );
    }

    @Test
    public void non_existing_ad_will_be_saved() {
        // in the db there is only one ad from katowice
        Ad scrappedAd = scrappedAd("wroclaw");
        adEvaluator.processAds(List.of(scrappedAd));

        Ad fromDb = adEvaluator.findInRepository(scrappedAd)
                .orElse(scrappedAd);
        assertEquals(fromDb.getCity(), "wroclaw");
    }

    @Test
    public void creation_date_of_refreshed_ad_will_be_updated() {
        Ad scrappedAd = scrappedAd("katowice");
        adEvaluator.processAds(List.of(scrappedAd));

        Ad fromDb = adEvaluator.findInRepository(scrappedAd)
                .orElse(scrappedAd);
        assertEquals(time.now().toLocalDate(), fromDb.getGumtreeCreationDate(), "Date has not been updated");
        assertEquals(3, fromDb.getUpdates().size());
    }

    private Ad scrappedAd(String city) {
        return Ad.builder()
                .city(city)
                .title("Modify this ad")
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of())
                .build();
    }
}
