package com.jvmops.gumtree.report;

import com.jvmops.gumtree.MongoTestClient;
import com.jvmops.gumtree.Time;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Slf4j
abstract class DataInitializer {

    @BeforeAll
    public static void clearData(
            @Autowired MongoTestClient mongoTestClient,
            @Autowired AdRepository adRepository,
            @Autowired Time time) {
        mongoTestClient.deleteAll();
        createFewAds(time).forEach(adRepository::save);
    }

    private static List<Ad> createFewAds(Time time) {
        Ad newAd = Ad.builder()
                .city("katowice")
                .title(UUID.randomUUID().toString())
                .description("Just added ad")
                .price(2400)
                .creationTime(time.now().minusHours(2))
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of(time.now().toLocalDate()))
                .build();
        Ad gasApartment = Ad.builder()
                .city("katowice")
                .title(UUID.randomUUID().toString())
                .description("Apartment with kuchnia gazowa (gas) in the kitchen")
                .price(2200)
                .creationTime(time.now().minusHours(23))
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of(time.now().toLocalDate()))
                .build();
        Ad oldAd = Ad.builder()
                .city("katowice")
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
