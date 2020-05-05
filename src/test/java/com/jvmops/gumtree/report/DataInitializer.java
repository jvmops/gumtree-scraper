package com.jvmops.gumtree.report;

import com.jvmops.gumtree.MongoTestClient;
import com.jvmops.gumtree.Time;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

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
        // Katowice
        LocalDateTime twoHoursBefore = time.now().minusHours(2);
        Ad newAdPolishLetters = Ad.builder()
                .city("Katowice")
                .title("Bieżąca oferta - dla leniucha - polskie znaki")
                .description("Fajne mieszkanie ze zmywarka")
                .price(2400)
                .creationTime(twoHoursBefore)
                .gumtreeCreationDate(twoHoursBefore.toLocalDate())
                .updates(List.of(twoHoursBefore.toLocalDate()))
                .build();
        LocalDateTime fourHoursBefore = time.now().minusHours(4);
        Ad newAdWithoutPolishLetters = Ad.builder()
                .city("Katowice")
                .title("Biezaca oferta - dla leniucha i kucharza")
                .description("Rudera, ale zmywarka jest i gaz też podłączony")
                .price(2400)
                .creationTime(fourHoursBefore)
                .gumtreeCreationDate(fourHoursBefore.toLocalDate())
                .updates(List.of(fourHoursBefore.toLocalDate()))
                .build();
        LocalDateTime twentyHoursBefore = time.now().minusHours(20);
        Ad gasApartment = Ad.builder()
                .city("Katowice")
                .title("Mieszkanie dla kucharza")
                .description("Mieszkanie z kuchenka gazowa")
                .price(2200)
                .creationTime(twentyHoursBefore)
                .gumtreeCreationDate(twentyHoursBefore.toLocalDate())
                .updates(List.of(twentyHoursBefore.toLocalDate()))
                .build();
        // more that 5 days old
        LocalDateTime twoWeeksBefore = time.now().minusWeeks(2);
        Ad oldAd = Ad.builder()
                .city("Katowice")
                .title("Stara, dlugo nieodswiezana oferta")
                .description("Fajne mieszkanie sprzed 2 tygodni")
                .price(700)
                .creationTime(twoWeeksBefore)
                .gumtreeCreationDate(twoWeeksBefore.toLocalDate())
                .updates(List.of(twoWeeksBefore.toLocalDate()))
                .build();

        // Wroclaw
        Ad wroclawAd = Ad.builder()
                .city("Wroclaw")
                .title("Wszystko czego potrzeba")
                .description("Mieszkanie we wroclawiu ze zmywarka i gazem")
                .price(700)
                .creationTime(twoHoursBefore)
                .gumtreeCreationDate(twoHoursBefore.toLocalDate())
                .updates(List.of(twoHoursBefore.toLocalDate()))
                .build();
        return List.of(newAdPolishLetters, newAdWithoutPolishLetters, gasApartment, oldAd, wroclawAd);
    }
}
