package com.jvmops.gumtree.scrapper.ads;

import com.jvmops.gumtree.scrapper.config.Selenium;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AdScrapper {
    private Selenium.WebDriverFactory webDriverFactory;
    private AdPreProcessor adPreProcessor;
    private AdEvaluator adEvaluator;
    private AdRepository adRepository;

    @PostConstruct
    public void scrapAds() {
        WebDriver webDriver = openAdsPage();

        List<AdSummary> scrappedAds = webDriver.findElement(By.className("results"))
                .findElement(By.className("view"))
                .findElements(By.className("tileV1"))
                .stream()
                .map(adPreProcessor::parseAdSummary)
                .collect(Collectors.toList());
        log.info("Fetched {} ads from page: {}", scrappedAds.size(), 1);

        scrappedAds.stream()
                .map(adSummary -> scrapAd(webDriver, adSummary))
                .map(adPreProcessor::parseAd)
                .filter(adEvaluator::isNew)
                .forEach(adRepository::save);
    }

    private ScrappedAd scrapAd(WebDriver webDriver, AdSummary adSummary) {
        webDriver.get(adSummary.getUrl());
        WebElement scrappedAd = webDriver.findElement(By.className("vip-details"));
        return ScrappedAd.builder()
                .adSummary(adSummary)
                .ad(scrappedAd)
                .build();
    }

    private WebDriver openAdsPage() {
        RemoteWebDriver webDriver = webDriverFactory.initialize();
        webDriver.get("https://www.gumtree.pl/s-mieszkania-i-domy-do-wynajecia/wroclaw/zmywarka/v1c9008l3200114q0p1");
        return webDriver;
    }
}
