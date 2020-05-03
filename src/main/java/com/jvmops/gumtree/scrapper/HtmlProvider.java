package com.jvmops.gumtree.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.util.StringUtils.capitalize;

@Component
@Slf4j
class HtmlProvider {
    public static final String GUMTREE_URL = "https://www.gumtree.pl";
    private static final String URL_TEMPLATE = "%s/s-mieszkania-i-domy-do-wynajecia/%s/v1c9008l3200114p%s";
    private static final String URL_TEMPLATE_PAGINATED = "%s/s-mieszkania-i-domy-do-wynajecia/%s/page-%s/v1c9008l3200114p%s";

    String adListing(String city, int pageNumber) {
        String url = getUrl(city, pageNumber);
        log.info("Scrapping {} ads from page {}... {}", capitalize(city), pageNumber, url);
        return get(url);
    }

    String adDetails(ListedAd listedAd) {
        return get(listedAd.getUrl());
    }

    private String getUrl(String city, int pageNumber) {
        // just for lolz :)
        String url =  switch (pageNumber) {
            case 1 -> String.format(URL_TEMPLATE, GUMTREE_URL, city, pageNumber);
            default -> String.format(URL_TEMPLATE_PAGINATED, GUMTREE_URL, city, pageNumber, pageNumber);
        };
        return url;
    }

    private String get(String url) {
        try {
            return Jsoup.connect(url).get().html();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}