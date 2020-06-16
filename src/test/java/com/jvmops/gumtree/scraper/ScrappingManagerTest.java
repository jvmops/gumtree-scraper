package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.ports.GumtreeAdScrapper;
import com.jvmops.gumtree.scraper.ports.ListedAdRepository;
import com.jvmops.gumtree.scraper.ports.UpdateRepository;
import com.jvmops.gumtree.subscriptions.model.City;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jvmops.gumtree.CustomClock.staticlyFixedClock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScrappingManagerTest {
    private static final LocalDate FIXED_DATE = LocalDate.now(staticlyFixedClock());

    @Mock
    GumtreeAdScrapper gumtreeAdScrapper;

    @Mock
    ListedAdRepository listedAdRepository;

    @Mock
    UpdateRepository updateRepository;

    ScrappingManager scrappingManager;

    @BeforeEach
    void setup() {
        scrappingManager = new ScrappingManager(gumtreeAdScrapper, listedAdRepository, updateRepository, staticlyFixedClock());
    }

    @Test
    void reposted_ad_is_being_reported() {
        var objectId = ObjectId.get();
        var listedAd = ListedAd.builder()
                .id(objectId)
                .gumtreeId("1")
                .title("Existing ad")
                .build();

        when(gumtreeAdScrapper.adListing(any()))
                .thenReturn(Stream.of(listedAd).collect(Collectors.toSet()));
        when(listedAdRepository.findByCityAndTitleIn(eq("Katowice"), Mockito.anySet()))
                .thenReturn(Set.of(listedAd));

        scrappingManager.scrapAds(new City("Katowice"));

        Mockito.verify(updateRepository)
                .updateSeenOn(eq(Set.of(objectId)), eq(LocalDate.now(staticlyFixedClock())));
    }
}
