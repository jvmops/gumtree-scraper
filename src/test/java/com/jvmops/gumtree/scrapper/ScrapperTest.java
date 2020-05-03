package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest(classes = Main.class)
public class ScrapperTest {

    private Scrapper scrapper;

    @Autowired
    private ListedAdRepository listedAdRepository;

    @Mock
    private HtmlProvider htmlProvider;

    @BeforeEach
    public void setup() {
        var adUrlBuilder = new AdUrlBuilder();
        JSoupAdListingScrapper adListingScrapper = new JSoupAdListingScrapper(htmlProvider, adUrlBuilder);
        JSoupAdDetailsScrapper adDetailsScrapper = new JSoupAdDetailsScrapper(htmlProvider);
        scrapper = new Scrapper(adListingScrapper, adDetailsScrapper, listedAdRepository);
    }

//    @Test
//    public void all_ads_will_be_scrapped() {
//        Set<Ad> ads = scrapper.scrapAds("Katowice");
//
//        Assert.assertEquals(22, ads.size());
//    }
}
