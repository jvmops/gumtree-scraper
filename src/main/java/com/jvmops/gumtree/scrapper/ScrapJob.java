package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
public class ScrapJob {
    private AdScrapper adScrapper;
    private AdEvaluator adEvaluator;

    void execute() {
        scrapGumtreeAds();
    }

    private void scrapGumtreeAds() {
        log.info("Scrap ads job started");
        List<Ad> scrappedAds = adScrapper.scrapAds();
        log.info("{} ads scrapped.  Processing them now...", scrappedAds.size());
        adEvaluator.processAds(scrappedAds);
    }
}
