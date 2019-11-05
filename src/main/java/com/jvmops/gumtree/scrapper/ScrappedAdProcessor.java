package com.jvmops.gumtree.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Lazy
@Slf4j
class ScrappedAdProcessor {
    ScrappedAdSummary parseAdSummary(WebElement webElement) {
        WebElement titleElement = webElement.findElement(By.className("title"));
        String url = titleElement.findElement(By.tagName("a")).getAttribute("href");
        String price = webElement.findElement(By.className("ad-price"))
                .getText()
                .replace("zł", "")
                .trim()
                .replace(" ", "");
        return ScrappedAdSummary.builder()
                .url(url)
                .title(titleElement.getText().trim())
                .price(Integer.valueOf(price))
                .build();
    }

    Ad parseAd(ScrappedAd scrapped) {
        WebElement ad = scrapped.getAd();
        String description = ad.findElement(By.className("description"))
                .getText();

        WebElement scrappedAdAttributes = ad.findElement(By.className("selMenu"));
        ScrappedAdAttributes adAttributes = parseAdAttributes(scrappedAdAttributes);

        return Ad.builder()
                .url(scrapped.getScrappedAdSummary().getUrl())
                .title(scrapped.getScrappedAdSummary().getTitle())
                .description(description)
                .price(scrapped.getScrappedAdSummary().getPrice())
                .availableSince(adAttributes.getAvailableSince())
                .landlord(adAttributes.getLandlord())
                .size(adAttributes.getSize())
                .gumtreeCreationDate(adAttributes.getCreationDate())
                .updates(List.of(adAttributes.getCreationDate()))
                .build();
    }

    private ScrappedAdAttributes parseAdAttributes(WebElement scrappedAdAttributes) {
        var adAttributes = scrappedAdAttributes.findElements(By.tagName("li")).stream()
                .map(this::findAttributeElement)
                .flatMap(Optional::stream)
                .map(this::parseAdAttribute)
                .collect(Collectors.toMap(
                        Pair::getFirst,
                        Pair::getSecond
                ));

        return new ScrappedAdAttributes(adAttributes);
    }

    private Optional<WebElement> findAttributeElement(WebElement element) {
        WebElement attributeElement = null;
        try {
            attributeElement = element.findElement(By.className("attribute"));
        } catch (NoSuchElementException ex) {
            element.findElement(By.className("advertisement-showup-banner"));
            // advertisement - just ignore
        }
        return Optional.ofNullable(attributeElement);
    }

    private Pair<String, String> parseAdAttribute(WebElement webElement) {
        String key = webElement.findElement(By.className("name")).getText();
        String value = webElement.findElement(By.className("value")).getText();
        return Pair.of(key, value);
    }

    @PostConstruct
    public void log() {
        log.warn("COMPONENT: ScrappedAdProcessor created!");
    }
}
