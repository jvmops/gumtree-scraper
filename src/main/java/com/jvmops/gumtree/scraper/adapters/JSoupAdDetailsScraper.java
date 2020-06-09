package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.Slowdown;
import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAdAttributes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
class JSoupAdDetailsScraper {
    private HtmlProvider htmlProvider;
    private Slowdown slowdown;
    private Clock clock;

    Optional<ScrappedAd> scrap(ListedAd listedAd) {
        slowdown.waitABit();

        log.debug("Scrapping - {} :: {}", listedAd.getTitle(), listedAd.getUrl());
        String adHtml = htmlProvider.adDetails(listedAd);
        Document adPage = Jsoup.parse(adHtml);
        String description = adPage.select("div.description")
                .get(0)
                .text();
        ScrappedAdAttributes adAttributes = scrapAttributes(adPage);

        ScrappedAd scrappedAd = ScrappedAd.builder()
                .city(listedAd.getCity())
                .gumtreeId(listedAd.getGumtreeId())
                .url(listedAd.getUrl())
                .title(listedAd.getTitle())
                .featured(listedAd.isFeatured())
                .description(description.toLowerCase())
                .price(listedAd.getPrice())
                .availableSince(adAttributes.getAvailableSince())
                .landlord(adAttributes.getLandlord())
                .size(adAttributes.getSize())
                .gumtreeCreationDate(adAttributes.getCreationDate())
                .seenOn(Set.of(LocalDate.now(clock)))
                .build();
        return Optional.of(scrappedAd);
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
                .map(this::scrapAttribute)
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

    private Pair<String, String> scrapAttribute(Element element) {
        String key = element.select("span.name").text();
        String value = element.select("span.value").text();
        return Pair.of(key, value);
    }
}
