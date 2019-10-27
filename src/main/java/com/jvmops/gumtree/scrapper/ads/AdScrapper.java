package com.jvmops.gumtree.scrapper.ads;

import com.jvmops.gumtree.scrapper.config.Selenium;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class AdScrapper {
    private Selenium.WebDriverFactory webDriverFactory;
    private AdPreProcessor adPreProcessor;

    public Stream<Ad> scrapAds() {
        WebDriver webDriver = openAdsPage();
        return webDriver.findElement(By.className("results"))
                .findElement(By.className("view"))
                .findElements(By.className("tileV1"))
                .stream()
                .map(adPreProcessor::parseAdSummary)
                .peek(adSummary -> log.info("Ad scrapped: {}", adSummary.getTitle()))
                .map(adSummary -> scrapAd(webDriver, adSummary))
                .map(adPreProcessor::parseAd);
    }

    private WebDriver openAdsPage() {
        RemoteWebDriver webDriver = webDriverFactory.initialize();
        webDriver.get("https://www.gumtree.pl/s-mieszkania-i-domy-do-wynajecia/wroclaw/zmywarka/v1c9008l3200114q0p1");
        return webDriver;
    }

    private ScrappedAd scrapAd(WebDriver webDriver, AdSummary adSummary) {
        webDriver.get(adSummary.getUrl());
        WebElement scrappedAd = webDriver.findElement(By.className("vip-details"));
        return ScrappedAd.builder()
                .adSummary(adSummary)
                .ad(scrappedAd)
                .build();
    }
}
