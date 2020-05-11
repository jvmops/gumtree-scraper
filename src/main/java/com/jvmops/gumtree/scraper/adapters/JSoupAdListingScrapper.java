package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.adapters.GumtreeUrlBuilder.AdUrl;
import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.subscriptions.City;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jvmops.gumtree.ScrapperConfig.DEFAULT_CURRENCY;

@Component
@AllArgsConstructor
@Slf4j
class JSoupAdListingScrapper {
    private HtmlProvider htmlProvider;
    private GumtreeUrlBuilder gumtreeUrlBuilder;

    List<ListedAd> scrap(City city, int pageNumber) {
        String adListingHtml = htmlProvider.adListing(city, pageNumber);
        Document adListing = Jsoup.parse(adListingHtml);

        List<ListedAd> featuredAds = featuredAds(adListing).stream()
                .map(ad -> parse(ad, city, true))
                .collect(Collectors.toList());
        log.debug("{} featured ads scrapped from page {}", featuredAds.size(), pageNumber);

        List<ListedAd> regularAds = regularAds(adListing).stream()
                .map(ad -> parse(ad, city))
                .collect(Collectors.toList());
        log.debug("{} regular ads scrapped from page {}", regularAds.size(), pageNumber);

        if (regularAds.size() == 0) {
            throw new EmptyAdListingPage(city, pageNumber);
        }

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

    private ListedAd parse(Element adFromList, City city) {
        return parse(adFromList, city, false);
    }

    private ListedAd parse(Element adFromList, City city, boolean featured) {
        Element title = adFromList.select("div.title").first();
        String urlSuffix = title.select("a[href]").attr("href");
        AdUrl adUrl = gumtreeUrlBuilder.buildAdUrl(urlSuffix);
        String priceSpanValue = adFromList.selectFirst("span.price-text").text();
        BigDecimal price = parse(priceSpanValue);

        return ListedAd.builder()
                .city(city.getName())
                .gumtreeId(adUrl.gumtreeId())
                .url(adUrl.url())
                .title(title.text().trim())
                .price(Money.of(DEFAULT_CURRENCY, price))
                .featured(featured)
                .build();
    }

    /**
     * It's always a natural number here
     */
    private BigDecimal parse(String priceSpanValue) {
        if (StringUtils.hasText(priceSpanValue)
                && priceSpanValue.contains("zł")) {
            String price = priceSpanValue.replace("zł", "")
                    .trim()
                    .replace(" ", ""); // "2 200"
            return new BigDecimal(price);
        } else {
            return BigDecimal.ZERO;
        }
    }

    static class EmptyAdListingPage extends RuntimeException {
        private static final String msg = "Not a single ad found for %s on page %s";

        public EmptyAdListingPage(City city, int pageNumber) {
            super(String.format(msg, city.getName(), pageNumber));
        }
    }
}
