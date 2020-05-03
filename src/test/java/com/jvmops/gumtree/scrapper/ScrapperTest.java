package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest(classes = Main.class)
public class ScrapperTest extends DataInitializer {

    private Scrapper scrapper;

    @Mock
    private HtmlProvider htmlProvider;

    @Autowired
    private ListedAdRepository listedAdRepository;

    @BeforeEach
    public void setup() {
        var adUrlBuilder = new AdUrlBuilder();
        JSoupAdListingScrapper adListingScrapper = new JSoupAdListingScrapper(htmlProvider, adUrlBuilder);
        JSoupAdDetailsScrapper adDetailsScrapper = new JSoupAdDetailsScrapper(htmlProvider);
        scrapper = new Scrapper(adListingScrapper, adDetailsScrapper, listedAdRepository);
        reloadApartments();
    }

    @Test
    public void there_is_13_listed_ads_on_page_3() {
        when(htmlProvider.adListing(anyString(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_3.getHtml()
        );

        Set<ListedAd> ads = scrapper.listing("Katowice");

        Assert.assertEquals(13, ads.size());
    }

    @Test
    public void maximum_of_10_pages_of_apartments_will_be_scrapped_with_duplicated_ads_removed_from_listing() {
        when(htmlProvider.adListing(anyString(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_1.getHtml() // otherwise it will fall in the endless loop
        );

        Set<ListedAd> ads = scrapper.listing("Katowice");

        Assert.assertEquals(13, ads.size());
    }

    @Test
    public void after_removing_already_saved_ads_from_page_3_there_will_be_12_listed_ads_to_be_scrapped() {
        when(htmlProvider.adListing(anyString(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_3.getHtml()
        );

        Set<ListedAd> ads = scrapper.filteredListing("Katowice");

        Assert.assertEquals(12, ads.size());
    }

    @Test
    public void there_will_be_31_ads_scrapped_from_3_pages() {
        when(htmlProvider.adListing(anyString(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_1.getHtml(),
                HtmlFile.AD_LISTING_PAGE_2.getHtml(), // duplicates at the beginning - offer was added during scrapping
                HtmlFile.AD_LISTING_PAGE_3.getHtml() // duplicates at the end - it means that next pages were already scrapped
        );
        when(htmlProvider.adDetails(any())).thenReturn(
                HtmlFile.AD_DETAILS.getHtml()
        );

        Set<Ad> ads = scrapper.scrapAds("Katowice");

        // 3x 10 regular ads = 30 + 3 featured = 33
        // 1 ad on page 2 comes from page 1 = 33 - 1 = 32
        // last ad on page 3 already exist in db = 32 - 1 = 31
        Assert.assertEquals(31, ads.size());
    }
}