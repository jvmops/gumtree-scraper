package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.ScrapperProperties;
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

    Set<Ad> scrapAds(String city) {
        Set<Ad> scrapped = filteredListing(city).stream()
                .map(adDetailsScrapper::scrap)
                .collect(Collectors.toSet());
        return scrapped;
    }

    Set<ListedAd> filteredListing(String city) {
        Set<ListedAd> listing = listing(city);
        Set<String> gumtreeIds = listing.stream()
                .map(ListedAd::getGumtreeId)
                .collect(Collectors.toSet());
        Set<ListedAd> duplicated = listedAdRepository.findByGumtreeIdIn(gumtreeIds);
        listing.removeAll(duplicated);
        log.info("{} ads left after removing duplicates that are existing in the db", listing.size());
        return listing;
    }

    Set<ListedAd> listing(String city) {
        Set<ListedAd> listedAds = new HashSet<>(60);
        int pageNumber = FIRST_PAGE;
        boolean scrapNextPage = true;
        while(pageNumber <= scrapperProperties.getMaxScrappedPages() && scrapNextPage) {
            List<ListedAd> page = adListingScrapper.scrap(city, pageNumber);
            listedAds.addAll(page);
            boolean alreadySavedExist = checkIfAlreadySavedExist(page);
            scrapNextPage = !alreadySavedExist;
            pageNumber++;
        }
        log.info("{} ads scrapped from {} pages", listedAds.size(), pageNumber-1);
        return listedAds;
    }

    private boolean checkIfAlreadySavedExist(List<ListedAd> ads) {
        Set<String> gumtreeIds = ads.stream()
                .filter(Predicate.not(ListedAd::isFeatured))
                .map(ListedAd::getGumtreeId)
                .collect(Collectors.toSet());
        return listedAdRepository.existsByGumtreeIdIn(gumtreeIds);
    }
}
