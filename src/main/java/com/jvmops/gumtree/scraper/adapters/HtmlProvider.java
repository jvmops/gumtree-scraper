package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.subscriptions.model.City;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@AllArgsConstructor
@Slf4j
class HtmlProvider {
    private final GumtreeUrlBuilder urlBuilder;

    String adListing(String cityCode) {
        URL url = urlBuilder.adListingUrl(cityCode);
        log.debug("Fetching ad listing using cityCode from: {}", url);
        return get(url);
    }

    String adListing(City city, int pageNumber) {
        URL url = urlBuilder.adListingUrl(city, pageNumber);
        log.debug("Fetching page {} of {} ad listing from: {}", pageNumber, city.getName(), url);
        return get(url);
    }

    String adDetails(ListedAd listedAd) {
        return get(listedAd.getUrl());
    }

    private String get(URL url) {
        try {
            return Jsoup.connect(url.toString()).get().html();
        } catch (IOException e) {
            log.error("Unable to fetch html from: {}", url, e);
            return "";
        }
    }
}