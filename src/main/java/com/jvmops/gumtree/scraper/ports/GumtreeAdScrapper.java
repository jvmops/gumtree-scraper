package com.jvmops.gumtree.scraper.ports;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.subscriptions.model.City;

import java.util.Optional;
import java.util.Set;

public interface GumtreeAdScrapper {
    Optional<ScrappedAd> adDetails(ListedAd listedAd);
    Set<ListedAd> adListing(City city);
}
