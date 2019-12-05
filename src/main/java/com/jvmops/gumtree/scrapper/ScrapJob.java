package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.config.GumtreeScrapperProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.springframework.util.StringUtils.capitalize;

@Profile("scrapper")
@Component
@Slf4j
@AllArgsConstructor
public class ScrapJob {
    public static final String GUMTREE_URL = "https://www.gumtree.pl";
    private static final String URL_TEMPLATE = "%s/s-mieszkania-i-domy-do-wynajecia/%s/zmywarka/v1c9008l3200114q0p1";

    private AdScrapper adScrapper;
    private AdEvaluator adEvaluator;
    private GumtreeScrapperProperties properties;

    @PostConstruct
    private void scrapAds() {
        properties.getCitiesToScrap().stream()
                .map(this::scrapAdsFrom)
                .forEach(adEvaluator::processAds);
    }

    private List<Ad> scrapAdsFrom(String city) {
        String url = String.format(URL_TEMPLATE, GUMTREE_URL, city);
        log.info("Scrapping ads from {}... {}", capitalize(city), url);
        return adScrapper.scrapAds(url);
    }
}
