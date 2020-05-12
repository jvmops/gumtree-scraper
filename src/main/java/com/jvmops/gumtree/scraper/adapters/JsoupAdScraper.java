package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.ScrapperProperties;
import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.ports.GumtreeAdScrapper;
import com.jvmops.gumtree.scraper.ports.ListedAdRepository;
import com.jvmops.gumtree.subscriptions.model.City;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
class JsoupAdScraper implements GumtreeAdScrapper {
    private static final int FIRST_PAGE = 1;

    private JSoupAdListingScraper adListingScrapper;
    private JSoupAdDetailsScraper adDetailsScrapper;
    private ListedAdRepository listedAdRepository;
    private ScrapperProperties scrapperProperties;

    @Override
    public Set<ListedAd> adListing(City city) {
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

    @Override
    public Optional<ScrappedAd> adDetails(ListedAd listedAd) {
        try {
            return adDetailsScrapper.scrap(listedAd);
        } catch (RuntimeException e) {
            log.error("Unable to scrap ad from: {}", listedAd.getUrl());
            return Optional.empty();
        }
    }

    private boolean checkIfAnyAlreadySaved(List<ListedAd> ads) {
        Set<String> gumtreeIds = ads.stream()
                .filter(Predicate.not(ListedAd::isFeatured))
                .map(ListedAd::getGumtreeId)
                .collect(Collectors.toSet());
        return listedAdRepository.existsByGumtreeIdIn(gumtreeIds);
    }
}
