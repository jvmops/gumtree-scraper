package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.ports.GumtreeAdScrapper;
import com.jvmops.gumtree.scraper.ports.ListedAdRepository;
import com.jvmops.gumtree.subscriptions.model.City;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class ScrappingManager {
    private GumtreeAdScrapper gumtreeAdScrapper;
    private ListedAdRepository listedAdRepository;

    public Set<ScrappedAd> scrapAds(City city) {
        Set<ScrappedAd> scrapped = filteredListing(city).stream()
                .map(gumtreeAdScrapper::adDetails)
                .collect(Collectors.toSet());
        log.info("Total of {} ads scrapped from {}", scrapped.size(), city.getName());
        return scrapped;
    }

    private Set<ListedAd> filteredListing(City city) {
        var listing = gumtreeAdScrapper.adListing(city);
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
}
