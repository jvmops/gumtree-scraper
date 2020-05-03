package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.scrapper.AdUrlBuilder.AdUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
class JSoupAdListingScrapper implements PriceParser {

    private final HtmlProvider htmlProvider;
    private final AdUrlBuilder adUrlBuilder;

    List<ListedAd> scrap(String city, int pageNumber) {
        String adListingHtml = htmlProvider.adListing(city, pageNumber);
        Document adListing = Jsoup.parse(adListingHtml);

        List<ListedAd> featuredAds = featuredAds(adListing).stream()
                .map(ad -> parse(ad, city, true))
                .collect(Collectors.toList());

        List<ListedAd> regularAds = regularAds(adListing).stream()
                .map(ad -> parse(ad, city))
                .collect(Collectors.toList());

        return Stream.of(regularAds, featuredAds)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
    }

    private Elements featuredAds(Document document) {
        return document.select("div.results")
                .select("div.topAdsContainer")
                .select("div.tileV1");
    }

    private Elements regularAds(Document document) {
        return document.select("div.results")
                .select("div.view")
                .select("div.tileV1");
    }

    private ListedAd parse(Element adFromList, String city) {
        return parse(adFromList, city, false);
    }

    private ListedAd parse(Element adFromList, String city, boolean featured) {
        Element title = adFromList.select("div.title").first();
        String urlSuffix = title.select("a[href]").attr("href");
        AdUrl adUrl = adUrlBuilder.buildAdUrl(urlSuffix);
        String priceSpanValue = adFromList.selectFirst("span.price-text").text();
        Integer price = parse(priceSpanValue);

        return ListedAd.builder()
                .city(city)
                .gumtreeId(adUrl.gumtreeId())
                .url(adUrl.url())
                .title(title.text().trim())
                .price(price)
                .featured(featured)
                .build();
    }
}
