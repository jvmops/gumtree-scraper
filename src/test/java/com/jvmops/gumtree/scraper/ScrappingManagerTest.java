package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.ports.GumtreeAdScrapper;
import com.jvmops.gumtree.scraper.ports.ListedAdRepository;
import com.jvmops.gumtree.subscriptions.model.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jvmops.gumtree.CustomClock.staticlyFixedClock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScrappingManagerTest {
    private static final LocalDate FIXED_DATE = LocalDate.now(staticlyFixedClock());

    @Mock
    GumtreeAdScrapper gumtreeAdScrapper;

    @Mock
    ListedAdRepository listedAdRepository;

    ScrappingManager scrappingManager;

    @BeforeEach
    void setup() {
        scrappingManager = new ScrappingManager(gumtreeAdScrapper, listedAdRepository, staticlyFixedClock());
    }

    @Test
    void reposted_ad_is_being_reported() {
        var spySet = Mockito.spy(new HashSet<LocalDate>());
        var listedAd = ListedAd.builder()
                .gumtreeId("1")
                .seenOn(spySet)
                .build();

        when(gumtreeAdScrapper.adListing(any()))
                .thenReturn(Stream.of(listedAd).collect(Collectors.toSet()));
        when(listedAdRepository.findByGumtreeIdIn(Set.of("1")))
                .thenReturn(Set.of(listedAd));

        scrappingManager.scrapAds(new City("Katowice"));

        Mockito.verify(spySet)
                .add(FIXED_DATE);
        Mockito.verify(listedAdRepository)
                .saveAll(Set.of(listedAd));
    }
}
