package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
class JSoupScrapper implements AdScrapper {
    private JSoupAdListingScrapper adListingParser;
    private JSoupAdDetailsScrapper adParser;

    @Override
    public List<ListedAd> scrapListing(String city, int pageNumber) {
        return adListingParser.scrap(city, pageNumber);
    }

    @Override
    public Ad scrapAd(ListedAd listedAd) {
        return adParser.scrap(listedAd);
    }
}

@Component
class HtmlProvider {
    String get(String url) {
        try {
            return Jsoup.connect(url).get().html();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
