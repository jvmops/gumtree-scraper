package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.city.City;
import com.jvmops.gumtree.city.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScrapJobTest {

    @Mock
    private CityService cityService;
    @Mock
    private Scrapper scrapper;
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

        verify(scrapper, times(2)).scrapAds(anyString());
    }

    @Test
    public void ad_evaluator_will_be_called_for_each_scrapped_city() {
        when(scrapper.scrapAds("Katowice")).thenReturn(Set.of(Ad.builder().gumtreeId("1").build(), Ad.builder().gumtreeId("2").build()));
        when(scrapper.scrapAds("Wroclaw")).thenReturn(Set.of(Ad.builder().gumtreeId("3").build()));

        scrapJob.scrapAds();

        verify(adEvaluator, times(3)).processAd(any());
    }

    private Set<City> stub() {
        City katowice = City.builder()
                .name("Katowice")
                .build();
        City wroclaw = City.builder()
                .name("Wroclaw")
                .build();
        return Set.of(katowice, wroclaw);
    }
}
