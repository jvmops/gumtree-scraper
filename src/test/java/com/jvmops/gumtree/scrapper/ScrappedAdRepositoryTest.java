package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @CompoundIndex acceptance test
 */
@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
public class ScrappedAdRepositoryTest extends DataInitializer {

    @Autowired
    ScrappedAdRepository scrappedAdRepository;

    @Test
    void saving_ad_with_the_same_city_and_title_will_voilate_unique_index() {
        assertThrows(
                DuplicateKeyException.class,
                () -> scrappedAdRepository.save(scrappedAd("wroclaw", "Takie sobie mieszkanie"))
        );
    }
}
