package com.jvmops.gumtree.scrapper.ads;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
class AdPreProcessor {
    AdSummary parseAdSummary(WebElement webElement) {
        WebElement titleElement = webElement.findElement(By.className("title"));
        String url = titleElement.findElement(By.tagName("a")).getAttribute("href");
        String price = webElement.findElement(By.className("ad-price"))
                .getText()
                .replace("zł", "")
                .trim()
                .replace(" ", "");
        return AdSummary.builder()
                .url(url)
                .title(titleElement.getText().trim())
                .price(Integer.valueOf(price))
                .build();
    }

    Ad parseAd(ScrappedAd scrappedAd) {
        WebElement ad = scrappedAd.getAd();
        String description = ad.findElement(By.className("description"))
                .getText();

        WebElement scrappedAdAttributes = ad.findElement(By.className("selMenu"));
        AdAttributes adAttributes = parseAdAttributes(scrappedAdAttributes);

        return Ad.builder()
                .url(scrappedAd.getAdSummary().getUrl())
                .title(scrappedAd.getAdSummary().getTitle())
                .description(description)
                .price(scrappedAd.getAdSummary().getPrice())
                .availableSince(adAttributes.getAvailableSince())
                .landlord(adAttributes.getLandlord())
                .size(adAttributes.getSize())
                .creationDate(adAttributes.getCreationDate())
                .updates(List.of(adAttributes.getCreationDate()))
                .build();
    }

    private AdAttributes parseAdAttributes(WebElement scrappedAdAttributes) {
        var adAttributes = scrappedAdAttributes.findElements(By.tagName("li")).stream()
                .map(this::findAttributeElement)
                .flatMap(Optional::stream)
                .map(this::parseAdAttribute)
                .collect(Collectors.toMap(
                        Pair::getFirst,
                        Pair::getSecond
                ));

        return new AdAttributes(adAttributes);
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
}
