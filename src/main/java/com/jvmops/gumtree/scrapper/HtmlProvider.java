package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.city.City;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
class HtmlProvider {
    public static final String GUMTREE_URL = "https://www.gumtree.pl";
    private static final String URL_TEMPLATE = "%s/s-mieszkania-i-domy-do-wynajecia/%sp%s";

    String adListing(City city, int pageNumber) {
        String url = String.format(URL_TEMPLATE, GUMTREE_URL, city.getUrlCode(), pageNumber);
        log.info("Scrapping {} ads from page {}... {}", city.getName(), pageNumber, url);
        return get(url);
    }

    String adDetails(ListedAd listedAd) {
        return get(listedAd.getUrl());
    }

    private String get(String url) {
        try {
            return Jsoup.connect(url).get().html();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}