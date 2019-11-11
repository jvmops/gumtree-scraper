package com.jvmops.gumtree.scrapper.selenium;

import com.jvmops.gumtree.config.Selenium;
import com.jvmops.gumtree.scrapper.Ad;
import com.jvmops.gumtree.scrapper.AdScrapper;
import com.jvmops.gumtree.scrapper.ListedAd;
import com.jvmops.gumtree.scrapper.ScrappedAdAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.data.util.Pair;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class AdScrapperSelenium implements AdScrapper {
    private Selenium.WebDriverFactory webDriverFactory;
    private SeleniumScrappedAdProcessor seleniumScrappedAdProcessor;

    @Override
    public List<Ad> scrapAds(String url) {
        WebDriver webDriver = openAdsPage(url);
        return scrapAds(webDriver).stream()
                .map(seleniumScrappedAdProcessor::parseAdSummary)
                .peek(listedAd -> log.debug("Ad scrapped: {}", listedAd.getTitle()))
                .map(listedAd -> scrapAd(webDriver, listedAd))
                .peek(scrappedAd -> log.debug("Ad details fetched"))
                .map(seleniumScrappedAdProcessor::parseAd)
                .peek(scrappedAd -> log.debug("Ad has been parsed"))
                .collect(Collectors.toList());
    }

    private List<WebElement> scrapAds(WebDriver webDriver) {
        log.info("Downloading ad list from a gumtree");
        return webDriver.findElement(By.className("results"))
                .findElement(By.className("view"))
                .findElements(By.className("tileV1"));
    }

    private WebDriver openAdsPage(String url) {
        RemoteWebDriver webDriver = webDriverFactory.initialize();
        webDriver.get(url);
        return webDriver;
    }

    private ScrappedAd scrapAd(WebDriver webDriver, ListedAd listedAd) {
        webDriver.get(listedAd.getUrl());
        WebElement scrappedAd = webDriver.findElement(By.className("vip-details"));
        return ScrappedAd.builder()
                .listedAd(listedAd)
                .ad(scrappedAd)
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

    @Getter
    @Builder
    static class ScrappedAd {
        private ListedAd listedAd;
        private WebElement ad;
    }


    @PostConstruct
    void log() {
        log.warn("Lazy SeleniumAdScrapper created.");
    }
}
