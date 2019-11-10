package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
class AdEvaluatorTest extends ScrapperDataInitializer {
    @Autowired
    private AdEvaluator adEvaluator;

    @Test
    public void creation_date_of_refreshed_ad_will_be_updated() {
        Ad scrappedAd = Ad.builder()
                .title("Modify this ad")
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of())
                .build();

        adEvaluator.processAds(List.of(scrappedAd));

        Ad fromDb = adEvaluator.findInRepository(scrappedAd)
                .orElse(scrappedAd);
        assertEquals(time.now().toLocalDate(), fromDb.getGumtreeCreationDate(), "Date has not been updated");
        assertEquals(3, fromDb.getUpdates().size());
    }
}
