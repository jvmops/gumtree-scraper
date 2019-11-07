package com.jvmops.gumtree;

import com.jvmops.gumtree.scrapper.Ad;
import com.jvmops.gumtree.scrapper.AdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class AdCollectionTest extends MongoTest {
    protected static final LocalDateTime TIME_NOW = LocalDateTime.now();
    protected static final LocalDate DATE_NOW = TIME_NOW.toLocalDate();
    protected static final LocalDateTime YESTERDAY = TIME_NOW.minusWeeks(2);
    protected static final LocalDateTime TWO_WEEKS_AGO = TIME_NOW.minusWeeks(2);
    protected static final LocalDateTime FIVE_WEEKS_AGO = TIME_NOW.minusWeeks(5);
    protected static final LocalDateTime FOUR_WEEKS_AGO = TIME_NOW.minusWeeks(4);

    @Autowired
    protected AdRepository adRepository;

    @BeforeEach
    public void setupDb() {
        insertDataIfNecessary();
    }

    private void insertDataIfNecessary() {
        if (adRepository.count() == 0) {
            createTestAds().forEach(adRepository::save);
        }
    }

    private Stream<Ad> createTestAds() {
        Ad newAd = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Just added ad")
                .price(2400)
                .creationTime(TIME_NOW.minusHours(2))
                .gumtreeCreationDate(DATE_NOW)
                .updates(List.of(DATE_NOW))
                .build();

        Ad gasApartment = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Apartment with kuchnia gazowa (gas) in the kitchen")
                .price(2200)
                .creationTime(TIME_NOW.minusHours(23))
                .gumtreeCreationDate(DATE_NOW)
                .updates(List.of(DATE_NOW))
                .build();

        Ad oldAd = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Some two week old ad")
                .price(700)
                .creationTime(TWO_WEEKS_AGO)
                .gumtreeCreationDate(TWO_WEEKS_AGO.toLocalDate())
                .updates(List.of(TWO_WEEKS_AGO.toLocalDate()))
                .build();


        List<LocalDate> updates = List.of(FIVE_WEEKS_AGO.toLocalDate(), FOUR_WEEKS_AGO.toLocalDate());
        Ad modifyThisAd = Ad.builder()
                .title("Modify this ad")
                .description("This ad can be modified during tests")
                .price(1800)
                .creationTime(FIVE_WEEKS_AGO)
                .modificationTime(FOUR_WEEKS_AGO)
                .gumtreeCreationDate(FIVE_WEEKS_AGO.toLocalDate())
                .updates(updates)
                .build();

        return Stream.of(newAd, gasApartment, oldAd, modifyThisAd);
    }
}
