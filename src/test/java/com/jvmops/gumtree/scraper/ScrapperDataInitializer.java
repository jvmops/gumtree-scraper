package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.ports.ScrappedAdRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.jvmops.gumtree.ScrapperConfig.DEFAULT_CURRENCY;

@Slf4j
public abstract class ScrapperDataInitializer {

    @Autowired
    protected ScrappedAdRepository scrappedAdRepository;
    @Autowired
    protected Clock clock;

    public void reloadApartments() {
        scrappedAdRepository.deleteAll();
        createTestAds().forEach(scrappedAdRepository::save);
    }

    protected ScrappedAd scrappedAd(String city, String title) {
        return ScrappedAd.builder()
                .city(city)
                .title(title)
                .gumtreeCreationDate(LocalDate.now(clock))
                .updates(List.of())
                .build();
    }

    private List<ScrappedAd> createTestAds() {
        LocalDateTime lastSeen = LocalDateTime.now(clock).minusWeeks(4);
        ScrappedAd thisWillBeModifiedInTest = ScrappedAd.builder()
                .city("Katowice")
                .gumtreeId("0000000")
                .title("Modify this ad")
                .description("This ad can be modified during tests")
                .price(Money.of(DEFAULT_CURRENCY, 1800))
                .creationTime(LocalDateTime.now(clock).minusWeeks(5))
                .modificationTime(lastSeen)
                .gumtreeCreationDate(lastSeen.toLocalDate())
                .updates(List.of(LocalDate.now(clock).minusWeeks(5), lastSeen.toLocalDate()))
                .build();

        ScrappedAd recentlyPostedApartmentInWroclaw = ScrappedAd.builder()
                .city("Wroclaw")
                .gumtreeId("1111111")
                .title("Takie sobie mieszkanie")
                .description("Ma miejsce parkingowe ale w srodku bieda")
                .price(Money.of(DEFAULT_CURRENCY, 2200))
                .creationTime(LocalDateTime.now(clock))
                .modificationTime(LocalDateTime.now(clock))
                .gumtreeCreationDate(LocalDate.now(clock))
                .updates(List.of(LocalDate.now(clock)))
                .build();

        ScrappedAd oldAdInKatowice = ScrappedAd.builder()
                .city("Katowice")
                .gumtreeId("10072336504490911177635309")
                .title("BEZ PROWIZJI- przestronna kawalerka w Centrum Katowic do wynajęcia")
                .description("Ma miejsce parkingowe ale w srodku bieda")
                .price(Money.of(DEFAULT_CURRENCY, 2500))
                .creationTime(LocalDateTime.now(clock))
                .modificationTime(LocalDateTime.now(clock))
                .gumtreeCreationDate(LocalDate.now(clock))
                .updates(List.of(LocalDate.now(clock)))
                .build();

        return List.of(thisWillBeModifiedInTest, recentlyPostedApartmentInWroclaw, oldAdInKatowice);
    }
}
