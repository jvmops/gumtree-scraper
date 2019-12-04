package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
class JSoupAdParser {
    private HtmlProvider htmlProvider;

    Ad scrap(ListedAd listedAd) {
        log.info("Scrapping - {} :: {}", listedAd.getTitle(), listedAd.getUrl());
        String adHtml = htmlProvider.get(listedAd.getUrl());
        Document adPage = Jsoup.parse(adHtml);
        String description = adPage.select("div.description").text();
        ScrappedAdAttributes adAttributes = scrapAttributes(adPage);

        return Ad.builder()
                .url(listedAd.getUrl())
                .title(listedAd.getTitle())
                .description(description)
                .price(listedAd.getPrice())
                .availableSince(adAttributes.getAvailableSince())
                .landlord(adAttributes.getLandlord())
                .size(adAttributes.getSize())
                .gumtreeCreationDate(adAttributes.getCreationDate())
                .updates(Collections.singletonList(adAttributes.getCreationDate()))
                .build();
    }

    private ScrappedAdAttributes scrapAttributes(Document adPage) {
        Element adDetails = adPage.select("div.vip-details").first();
        Elements scrappedAdAttributes = adDetails.select("ul")
                .select("li");
        return parseAttributes(scrappedAdAttributes);
    }

    private ScrappedAdAttributes parseAttributes(Elements scrappedAdAttributes) {
        Map<String, String> adAttributes = scrappedAdAttributes.stream()
                .map(this::findAttributeElement)
                .flatMap(Optional::stream)
                .map(this::parseAttribute)
                .collect(Collectors.toMap(
                        Pair::getFirst,
                        Pair::getSecond
                ));
        return new ScrappedAdAttributes(adAttributes);
    }

    private Optional<Element> findAttributeElement(Element element) {
        Element attributeElement = null;
        try {
            attributeElement = element.select("div.attribute").first();
        } catch (NoSuchElementException ex) {
            element.select("div.advertisement-showup-banner");
            // advertisement - just ignore
        }
        return Optional.ofNullable(attributeElement);
    }

    private Pair<String, String> parseAttribute(Element element) {
        String key = element.select("span.name").text();
        String value = element.select("span.value").text();
        return Pair.of(key, value);
    }
}
