package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.ScrapperProperties;
import com.jvmops.gumtree.scraper.HtmlFile;
import com.jvmops.gumtree.scraper.ScrapperDataInitializer;
import com.jvmops.gumtree.scraper.ScrappingManager;
import com.jvmops.gumtree.scraper.Slowdown;
import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.ports.GumtreeAdScrapper;
import com.jvmops.gumtree.scraper.ports.ListedAdRepository;
import com.jvmops.gumtree.subscriptions.model.City;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

/**
 * TODO: move this test to main package (another layer of abstraction needed for mocks)
 */
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest(classes = Main.class)
public class AdScrappingTest extends ScrapperDataInitializer {

    private GumtreeAdScrapper scrapper;
    private ScrappingManager scrappingManager;

    @Mock
    private HtmlProvider htmlProvider;

    @Autowired
    private GumtreeUrlBuilder gumtreeUrlBuilder;
    @Autowired
    private Slowdown slowdown;
    @Autowired
    private ListedAdRepository listedAdRepository;
    @Autowired
    private ScrapperProperties scrapperProperties;


    @BeforeEach
    public void setup() {
        JSoupAdListingScrapper adListingScrapper = new JSoupAdListingScrapper(htmlProvider, gumtreeUrlBuilder);
        JSoupAdDetailsScrapper jSoupAdDetailsScrapper = new JSoupAdDetailsScrapper(htmlProvider, slowdown);
        scrapper = new JsoupAdScrapper(
                adListingScrapper,
                jSoupAdDetailsScrapper,
                listedAdRepository,
                scrapperProperties);
        scrappingManager = new ScrappingManager(scrapper, listedAdRepository);
        reloadApartments();
    }

    @Test
    public void there_is_13_listed_ads_on_page_3() {
        when(htmlProvider.adListing(any(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_3.getHtml()
        );

        Set<ListedAd> ads = scrapper.adListing(katowice());

        Assert.assertEquals(13, ads.size());
    }

    @Test
    public void maximum_of_3_pages_of_apartments_will_be_scrapped_with_duplicated_ads_removed_from_listing() {
        // limit comes from ScrapperProperties
        when(htmlProvider.adListing(any(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_1.getHtml() // otherwise it will fall in the endless loop
        );

        Set<ListedAd> ads = scrapper.adListing(katowice());

        Assert.assertEquals(13, ads.size());
    }

    @Test
    public void after_removing_already_saved_ads_from_page_3_there_will_be_12_listed_ads_to_be_scrapped() {
        when(htmlProvider.adListing(any(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_3.getHtml()
        );
        when(htmlProvider.adDetails(any())).thenReturn(
                HtmlFile.AD_DETAILS.getHtml()
        );

        Set<ScrappedAd> ads = scrappingManager.scrapAds(katowice());

        Assert.assertEquals(12, ads.size());
    }

    @Test
    public void there_will_be_31_ads_scrapped_from_3_pages() {
        when(htmlProvider.adListing(any(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_1.getHtml(),
                HtmlFile.AD_LISTING_PAGE_2.getHtml(),
                HtmlFile.AD_LISTING_PAGE_3.getHtml() // duplicates at the end - it means that next pages were already scrapped
        );
        when(htmlProvider.adDetails(any())).thenReturn(
                HtmlFile.AD_DETAILS.getHtml()
        );

        Set<ScrappedAd> ads = scrappingManager.scrapAds(katowice());

        // 3x 10 regular ads = 30 + 3 featured = 33
        // 1 ad on page 2 comes from page 1 = 33 - 1 = 32
        // last ad on page 3 already exist in db = 32 - 1 = 31
        Assert.assertEquals(31, ads.size());
    }

    private static City katowice() {
        return City.builder()
                .name("Katowice")
                .code("v1c9008l3200285")
                .build();
    }
}
