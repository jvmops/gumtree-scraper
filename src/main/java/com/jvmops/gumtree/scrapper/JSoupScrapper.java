package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.config.GumtreeScrapperProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
class JSoupScrapper implements AdScrapper {
    private JSoupAdListingParser adListingParser;
    private JSoupAdParser adParser;

    @Override
    public List<Ad> scrapAds(String url) {
        return adListingParser.scrap(url).stream()
                .map(adParser::scrap)
                .collect(Collectors.toUnmodifiableList());
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
