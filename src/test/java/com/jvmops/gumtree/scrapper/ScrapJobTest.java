//package com.jvmops.gumtree.scrapper;
//
//import com.jvmops.gumtree.Main;
//import com.jvmops.gumtree.city.City;
//import com.jvmops.gumtree.city.CityService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Set;
//
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(classes = Main.class)
//public class ScrapJobTest {
//
//    @Mock
//    private CityService cityService;
//    @Mock
//    private AdScrapper adScrapper;
//    @Mock
//    private AdEvaluator adEvaluator;
//
//    private ScrapJob scrapJob;
//
//    @BeforeEach
//    public void setupCities() {
//        when(cityService.cities()).thenReturn(stub());
//        scrapJob = new ScrapJob(adScrapper, adEvaluator, cityService);
//    }
//
//    @Test
//    public void scrapper_will_be_called_for_each_existing_city() {
//        scrapJob.scrapAds();
//        verify(adScrapper, times(2)).scrapAd(anyString());
//    }
//
//    @Test
//    public void ad_evaluator_will_be_called_for_each_scrapped_city() {
//        scrapJob.scrapAds();
//        verify(adEvaluator, times(2)).processAds(anyList());
//    }
//
//    Set<City> stub() {
//        City katowice = City.builder()
//                .name("Katowice")
//                .build();
//        City wroclaw = City.builder()
//                .name("Wroclaw")
//                .build();
//        return Set.of(katowice, wroclaw);
//    }
//}
