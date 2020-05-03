package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.city.City;
import com.jvmops.gumtree.city.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Profile("scrapper")
@Component
@Slf4j
@AllArgsConstructor
public class ScrapJob {
    private static final int FIRST_PAGE = 1;

    private AdScrapper adScrapper;
    private AdEvaluator adEvaluator;
    private ScrappedAdRepository scrappedAdRepository;
    private CityService cityService;

    @PostConstruct
    void scrapAds() {
        cityService.cities().stream()
                .map(City::getName)
                .map(this::getListing)
                .map(this::scrapAds)
                .flatMap(Collection::stream)
                .forEach(adEvaluator::processAd);
    }

    Set<ListedAd> getListing(String city) {
        Set<ListedAd> listedAds = new HashSet<>(60);
        int pageNumber = FIRST_PAGE;
        boolean scrapNextPage = true;
        while(scrapNextPage) {
            List<ListedAd> page = adScrapper.scrapListing(city, pageNumber);
            listedAds.addAll(page);
            boolean alreadySavedExist = skipFirstFiveAndCheckIfAlreadySavedExist(page);
            scrapNextPage = !alreadySavedExist;
        }
        return listedAds;
    }

    private boolean skipFirstFiveAndCheckIfAlreadySavedExist(List<ListedAd> ads) {
        Set<String> gumtreeIds = ads.stream()
                .filter(Predicate.not(ListedAd::isFeatured))
                .skip(5) // ad might be added during reading multiple pages
                .map(ListedAd::getGumtreeId)
                .collect(Collectors.toSet());
        return scrappedAdRepository.existsByGumtreeIds(gumtreeIds);
    }

    private Set<Ad> scrapAds(Set<ListedAd> listedAds) {
        return listedAds.stream()
                .map(adScrapper::scrapAd)
                .collect(Collectors.toUnmodifiableSet());
    }
}
