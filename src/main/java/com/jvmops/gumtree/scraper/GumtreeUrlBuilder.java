package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.subscriptions.City;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

@Component
class GumtreeUrlBuilder {
    public static final String GUMTREE_URL = "https://www.gumtree.pl";
    private static final String URL_TEMPLATE = "%s/s-mieszkania-i-domy-do-wynajecia/%sp%s";

    URL adListingUrl(String cityCode) {
        String url = String.format(URL_TEMPLATE, GUMTREE_URL, cityCode, 1);
        return parseUrl(url);
    }

    URL adListingUrl(City city, int pageNumber) {
        String url = String.format(URL_TEMPLATE, GUMTREE_URL, city.getCode(), pageNumber);
        return parseUrl(url);
    }

    AdUrl buildAdUrl(String urlSuffix) {
        String[] urlParts = urlSuffix.split("/");
        String gumtreeId = urlParts[urlParts.length-1];
        StringBuilder urlBuilder = new StringBuilder(GUMTREE_URL);
        for (int i = 0; i < urlParts.length -2; i++) {
            if (StringUtils.isEmpty(urlParts[i])) {
                continue;
            }
            urlBuilder.append("/" + urlParts[i]);
        }
        urlBuilder.append("/" + gumtreeId);
        URL url = parseUrl(urlBuilder.toString());
        return new AdUrl(gumtreeId, url);
    }

    static URL parseUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(String.format("Unable to parse url: %s", url));
        }
    }

    record AdUrl(String gumtreeId, URL url) {}
}
