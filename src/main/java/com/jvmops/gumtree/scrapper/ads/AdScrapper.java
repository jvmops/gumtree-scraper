package com.jvmops.gumtree.scrapper.ads;

import com.jvmops.gumtree.scrapper.config.Selenium;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
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

    @PostConstruct
    public void scrapAds() {
        RemoteWebDriver webDriver = openAdsPage();

        List<WebElement> scrappedAds = webDriver.findElement(By.className("list-view"))
                .findElement(By.className("view"))
                .findElements(By.className("tileV1"));
        log.info("Fetched {} ads from page: {}", scrappedAds.size(), 1);

        List<AdSummary> adSummaries = scrappedAds.stream()
                .map(adPreProcessor::parseAdSummary)
                .collect(Collectors.toList());
    }

    private RemoteWebDriver openAdsPage() {
        RemoteWebDriver webDriver = webDriverFactory.initialize();
        webDriver.get("https://www.gumtree.pl/s-mieszkania-i-domy-do-wynajecia/wroclaw/mieszkanie/v1c9008l3200114a1dwp1");
        return webDriver;
    }
}
