package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.AdEvaluator;
import com.jvmops.gumtree.scraper.ScrappingManager;
import com.jvmops.gumtree.subscriptions.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Profile("scraper")
@Component
@Slf4j
@AllArgsConstructor
class ScrapJob {
    private CityService cityService;
    private ScrappingManager scrapper;
    private AdEvaluator adEvaluator;

    @PostConstruct
    void scrapAds() {
        cityService.cities().stream()
                .peek(city -> log.info("Scrapping ads from {} started", city.getName()))
                .map(scrapper::scrapAds)
                .flatMap(Collection::stream)
                .forEach(adEvaluator::processAd);
    }
}
