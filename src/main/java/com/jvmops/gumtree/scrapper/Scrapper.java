package com.jvmops.gumtree.scrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Scrapper {
    private static final int FIRST_PAGE = 1;

    private final JSoupAdListingScrapper adListingScrapper;
    private final JSoupAdDetailsScrapper  adDetailsScrapper;
    private final ListedAdRepository listedAdRepository;

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
        return listing;
    }

    Set<ListedAd> listing(String city) {
        Set<ListedAd> listedAds = new HashSet<>(60);
        int pageNumber = FIRST_PAGE;
        boolean scrapNextPage = true;
        while(pageNumber <= 10 && scrapNextPage) {
            List<ListedAd> page = adListingScrapper.scrap(city, pageNumber);
            listedAds.addAll(page);
            boolean alreadySavedExist = skipFirstFiveAndCheckIfAlreadySavedExist(page);
            scrapNextPage = !alreadySavedExist;
            pageNumber++;
        }
        return listedAds;
    }

    private boolean skipFirstFiveAndCheckIfAlreadySavedExist(List<ListedAd> ads) {
        Set<String> gumtreeIds = ads.stream()
                .filter(Predicate.not(ListedAd::isFeatured))
                .skip(5) // ad might be added during reading multiple pages
                .map(ListedAd::getGumtreeId)
                .collect(Collectors.toSet());
        return listedAdRepository.existsByGumtreeIdIn(gumtreeIds);
    }
}
