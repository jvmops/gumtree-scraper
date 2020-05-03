package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Time;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
abstract class DataInitializer {

    @Autowired
    protected ScrappedAdRepository scrappedAdRepository;
    @Autowired
    protected Time time;

    void reloadApartments() {
        scrappedAdRepository.deleteAll();
        createTestAds().forEach(scrappedAdRepository::save);
    }

    protected Ad scrappedAd(String city, String title) {
        return Ad.builder()
                .city(city)
                .title(title)
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of())
                .build();
    }

    private List<Ad> createTestAds() {
        LocalDateTime lastSeen = time.now().minusWeeks(4);
        Ad thisWillBeModifiedInTest = Ad.builder()
                .city("Katowice")
                .gumtreeId("0000000")
                .title("Modify this ad")
                .description("This ad can be modified during tests")
                .price(1800)
                .creationTime(time.now().minusWeeks(5))
                .modificationTime(lastSeen)
                .gumtreeCreationDate(lastSeen.toLocalDate())
                .updates(List.of(time.now().minusWeeks(5).toLocalDate(), lastSeen.toLocalDate()))
                .build();

        Ad recentlyPostedApartmentInWroclaw = Ad.builder()
                .city("Wroclaw")
                .gumtreeId("1111111")
                .title("Takie sobie mieszkanie")
                .description("Ma miejsce parkingowe ale w srodku bieda")
                .price(2200)
                .creationTime(time.now())
                .modificationTime(time.now())
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of(time.now().toLocalDate()))
                .build();

        Ad oldAdInKatowice = Ad.builder()
                .city("Katowice")
                .gumtreeId("10072336504490911177635309")
                .title("BEZ PROWIZJI- przestronna kawalerka w Centrum Katowic do wynajęcia")
                .description("Ma miejsce parkingowe ale w srodku bieda")
                .price(2500)
                .creationTime(time.now())
                .modificationTime(time.now())
                .gumtreeCreationDate(time.now().toLocalDate())
                .updates(List.of(time.now().toLocalDate()))
                .build();

        return List.of(thisWillBeModifiedInTest, recentlyPostedApartmentInWroclaw, oldAdInKatowice);
    }
}
