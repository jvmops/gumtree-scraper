package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @CompoundIndex acceptance test
 */
@SpringBootTest(classes = Main.class)
public class ScrappedAdRepositoryTest extends DataInitializer {

    @Autowired
    ScrappedAdRepository scrappedAdRepository;

    @BeforeEach
    public void setup() {
        reloadApartments();
    }

    @Test
    void saving_ad_with_the_same_city_and_title_will_voilate_unique_index() {
        assertThrows(
                DuplicateKeyException.class,
                () -> scrappedAdRepository.save(scrappedAd("Wroclaw", "Takie sobie mieszkanie"))
        );
    }
}
