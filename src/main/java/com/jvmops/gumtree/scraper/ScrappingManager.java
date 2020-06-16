package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.ports.GumtreeAdScrapper;
import com.jvmops.gumtree.scraper.ports.ListedAdRepository;
import com.jvmops.gumtree.scraper.ports.UpdateRepository;
import com.jvmops.gumtree.subscriptions.model.City;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
@AllArgsConstructor
public class ScrappingManager {
    private GumtreeAdScrapper gumtreeAdScrapper;
    private ListedAdRepository listedAdRepository;
    private UpdateRepository updateRepository;
    private Clock clock;

    public Set<ScrappedAd> scrapAds(City city) {
        Set<ScrappedAd> scrapped = filteredListing(city).stream()
                .map(gumtreeAdScrapper::adDetails)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
        log.info("Total of {} ads scrapped from {}", scrapped.size(), city.getName());
        return scrapped;
    }

    private Set<ListedAd> filteredListing(City city) {
        var listing = gumtreeAdScrapper.adListing(city);
        log.info("Retrieved {} offers from a {} listing", listing.size(), city.getName());
        Set<String> titles = listing.stream()
                .map(ListedAd::getTitle)
                .collect(Collectors.toSet());
        Set<ListedAd> duplicated = listedAdRepository.findByCityAndTitleIn(city.getName(), titles);
        if ( ! isEmpty(duplicated) ) {
            markThatTheyWereReposted(duplicated);
            listing.removeAll(duplicated);
            log.info("{} offers left after removing duplicates from a {} listing", listing.size(), city.getName());
        }
        return listing;
    }

    private void markThatTheyWereReposted(Set<ListedAd> duplicated) {
        Set<ObjectId> ids = duplicated.stream()
                .map(ListedAd::getId)
                .collect(Collectors.toSet());
        LocalDate today = LocalDate.now(clock);
        updateRepository.updateSeenOn(ids, today);
    }
}
