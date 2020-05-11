package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.AdEvaluator;
import com.jvmops.gumtree.scraper.ScrappingManager;
import com.jvmops.gumtree.scraper.adapters.ScrapJob;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.subscriptions.City;
import com.jvmops.gumtree.subscriptions.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScrapJobTest {

    @Mock
    private CityService cityService;
    @Mock
    private ScrappingManager scrapper;
    @Mock
    private AdEvaluator adEvaluator;

    private ScrapJob scrapJob;

    @BeforeEach
    public void setup() {
        scrapJob = new ScrapJob(cityService, scrapper, adEvaluator);
        when(cityService.cities()).thenReturn(stub());
    }

    @Test
    public void scrapper_will_be_called_for_each_existing_city() {
        scrapJob.scrapAds();

        verify(scrapper, times(2)).scrapAds(any());
    }

    @Test
    public void ad_evaluator_will_be_called_for_each_scrapped_city() {
        when(scrapper.scrapAds(katowice())).thenReturn(Set.of(ScrappedAd.builder().gumtreeId("1").build(), ScrappedAd.builder().gumtreeId("2").build()));
        when(scrapper.scrapAds(wroclaw())).thenReturn(Set.of(ScrappedAd.builder().gumtreeId("3").build()));

        scrapJob.scrapAds();

        verify(adEvaluator, times(3)).processAd(any());
    }

    private static Set<City> stub() {
        return Set.of(katowice(), wroclaw());
    }

    private static City katowice() {
        return City.builder()
                .name("Katowice")
                .code("v1c9008l3200285")
                .build();
    }

    private static City wroclaw() {
        return City.builder()
                .name("Wroclaw")
                .code("v1c9008l3204208")
                .build();
    }
}
