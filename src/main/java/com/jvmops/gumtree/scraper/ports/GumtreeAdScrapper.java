package com.jvmops.gumtree.scraper.ports;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.subscriptions.City;

import java.util.Set;

public interface GumtreeAdScrapper {
    ScrappedAd adDetails(ListedAd listedAd);
    Set<ListedAd> adListing(City city);
}
