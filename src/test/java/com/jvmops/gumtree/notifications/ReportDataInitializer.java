package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.MongoTest;
import com.jvmops.gumtree.config.Time;
import com.jvmops.gumtree.scrapper.Ad;
import com.jvmops.gumtree.scrapper.AdRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class ReportDataInitializer extends MongoTest {
    @Autowired
    private AdRepository adRepository;
    @Autowired
    protected Time time;

    @BeforeAll
    public static void setupDb() {
        deleteAll();
    }

    @BeforeEach
    private void insertDataIfNecessary() {
        if (adRepository.count() == 0) {
            log.info("Inserting test data for report");
            createFewAds().forEach(adRepository::save);
        }
    }

    private List<Ad> createFewAds() {
        Ad newAd = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Just added ad")
                .price(2400)
                .creationTime(time.now().minusHours(2))
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of(time.now().toLocalDate()))
                .build();

        Ad gasApartment = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Apartment with kuchnia gazowa (gas) in the kitchen")
                .price(2200)
                .creationTime(time.now().minusHours(23))
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of(time.now().toLocalDate()))
                .build();

        Ad oldAd = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Some two week old ad")
                .price(700)
                .creationTime(time.now().minusWeeks(2))
                .gumtreeCreationDate(time.now().minusWeeks(2).toLocalDate())
                .updates(List.of(time.now().minusWeeks(2).toLocalDate()))
                .build();

        return List.of(newAd, gasApartment, oldAd);
    }
}
