package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.capitalize;

@Component
@Slf4j
@AllArgsConstructor
class JSoupAdListingScrapper implements PriceParser {
    public static final String GUMTREE_URL = "https://www.gumtree.pl";
    private static final String URL_TEMPLATE = "%s/s-mieszkania-i-domy-do-wynajecia/%s/v1c9008l3200114q0p1";
    private static final String URL_TEMPLATE_PAGINATED = "%s/s-mieszkania-i-domy-do-wynajecia/%s/page-%s/v1c9008l3200114q0p1";

    private final HtmlProvider htmlProvider;

    List<ListedAd> scrap(String city, int pageNumber) {
        String url = getUrl(city, pageNumber);

        log.info("Scrapping {} ads from page {}... {}", capitalize(city), pageNumber, url);

        String adListingHtml = htmlProvider.get(url);
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

    private String getUrl(String city, int pageNumber) {
        // just for lolz :)
        String url =  switch (pageNumber) {
            case 1 -> String.format(URL_TEMPLATE, GUMTREE_URL, city);
            default -> String.format(URL_TEMPLATE_PAGINATED, GUMTREE_URL, city, pageNumber);
        };
        return url;
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
        String url = title.select("a[href]").attr("href");

        String[] urlParts = url.split("/");
        String gumtreeId = urlParts[urlParts.length-1];
        StringBuilder urlBuilder = new StringBuilder(GUMTREE_URL);
        for (int i = 0; i < urlParts.length -2; i++) {
            if (StringUtils.isEmpty(urlParts[i])) {
                continue;
            }
            urlBuilder.append("/" + urlParts[i]);
        }
        urlBuilder.append("/" + gumtreeId);

        String priceSpanValue = adFromList.selectFirst("span.price-text").text();
        Integer price = parse(priceSpanValue);

        return ListedAd.builder()
                .city(city)
                .gumtreeId(gumtreeId)
                .url(urlBuilder.toString())
                .title(title.text().trim())
                .price(price)
                .featured(featured)
                .build();
    }
}
