package com.jvmops.gumtree.scraper;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityUrlCodeValidator {
    private final HtmlProvider htmlProvider;

    public boolean isValid(String cityCode) {
        String html = htmlProvider.adListing(cityCode);
        Document document = Jsoup.parse(html);
        return document.title().contains("Mieszkania i domy do wynajęcia");
    }
}
