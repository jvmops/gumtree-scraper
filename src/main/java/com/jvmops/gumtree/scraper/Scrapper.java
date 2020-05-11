package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.ScrapperProperties;
import com.jvmops.gumtree.subscriptions.City;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class Scrapper {
    private static final int FIRST_PAGE = 1;

    private final JSoupAdListingScrapper adListingScrapper;
    private final JSoupAdDetailsScrapper  adDetailsScrapper;
    private final ListedAdRepository listedAdRepository;
    private final ScrapperProperties scrapperProperties;

    Set<Ad> scrapAds(City city) {
        Set<Ad> scrapped = filteredListing(city).stream()
                .map(adDetailsScrapper::scrap)
                .collect(Collectors.toSet());
        log.info("Total of {} ads scrapped from {}", scrapped.size(), city.getName());
        return scrapped;
    }

    Set<ListedAd> filteredListing(City city) {
        Set<ListedAd> listing = listing(city);
        log.info("Retrieved {} offers from a {} listing", listing.size(), city.getName());
        Set<String> gumtreeIds = listing.stream()
                .map(ListedAd::getGumtreeId)
                .collect(Collectors.toSet());
        Set<ListedAd> duplicated = listedAdRepository.findByGumtreeIdIn(gumtreeIds);
        if (listing.removeAll(duplicated)) {
            log.info("{} offers left after removing duplicates from a {} listing", listing.size(), city.getName());
        }
        return listing;
    }

    Set<ListedAd> listing(City city) {
        Set<ListedAd> listedAds = new HashSet<>(60);
        int pageNumber = FIRST_PAGE;
        boolean scrapNextPage = true;
        while(pageNumber <= scrapperProperties.getMaxScrappedPages() && scrapNextPage) {
            List<ListedAd> page = adListingScrapper.scrap(city, pageNumber);
            listedAds.addAll(page);
            boolean alreadySavedExist = checkIfAnyAlreadySaved(page);
            scrapNextPage = !alreadySavedExist;
            pageNumber++;
        }
        return listedAds;
    }

    private boolean checkIfAnyAlreadySaved(List<ListedAd> ads) {
        Set<String> gumtreeIds = ads.stream()
                .filter(Predicate.not(ListedAd::isFeatured))
                .map(ListedAd::getGumtreeId)
                .collect(Collectors.toSet());
        return listedAdRepository.existsByGumtreeIdIn(gumtreeIds);
    }
}
