package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.config.Selenium;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

@Service
@Lazy
@Slf4j
@AllArgsConstructor
public class AdScrapper {
    private Selenium.WebDriverFactory webDriverFactory;
    private ScrappedAdProcessor scrappedAdProcessor;

    public Stream<Ad> scrapAds() {
        WebDriver webDriver = openAdsPage();
        return webDriver.findElement(By.className("results"))
                .findElement(By.className("view"))
                .findElements(By.className("tileV1"))
                .stream()
                .map(scrappedAdProcessor::parseAdSummary)
                .peek(scrappedAdSummary -> log.info("Ad scrapped: {}", scrappedAdSummary.getTitle()))
                .map(scrappedAdSummary -> scrapAd(webDriver, scrappedAdSummary))
                .map(scrappedAdProcessor::parseAd);
    }

    private WebDriver openAdsPage() {
        RemoteWebDriver webDriver = webDriverFactory.initialize();
        webDriver.get("https://www.gumtree.pl/s-mieszkania-i-domy-do-wynajecia/wroclaw/zmywarka/v1c9008l3200114q0p1");
        return webDriver;
    }

    private ScrappedAd scrapAd(WebDriver webDriver, ScrappedAdSummary scrappedAdSummary) {
        webDriver.get(scrappedAdSummary.getUrl());
        WebElement scrappedAd = webDriver.findElement(By.className("vip-details"));
        return ScrappedAd.builder()
                .scrappedAdSummary(scrappedAdSummary)
                .ad(scrappedAd)
                .build();
    }

    @PostConstruct
    public void log() {
        log.warn("Lazy AdScrapper created.");
    }
}
