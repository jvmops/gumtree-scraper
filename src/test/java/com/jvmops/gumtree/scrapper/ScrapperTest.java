package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.ScrapperProperties;
import com.jvmops.gumtree.city.City;
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

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest(classes = Main.class)
public class ScrapperTest extends DataInitializer {

    private Scrapper scrapper;

    @Mock
    private HtmlProvider htmlProvider;

    @Autowired
    private AdUrlBuilder adUrlBuilder;
    @Autowired
    private ListedAdRepository listedAdRepository;
    @Autowired
    private Slowdown slowdown;
    @Autowired
    private ScrapperProperties scrapperProperties;

    @BeforeEach
    public void setup() {
        JSoupAdListingScrapper adListingScrapper = new JSoupAdListingScrapper(htmlProvider, adUrlBuilder);
        JSoupAdDetailsScrapper adDetailsScrapper = new JSoupAdDetailsScrapper(htmlProvider, slowdown);
        scrapper = new Scrapper(adListingScrapper, adDetailsScrapper, listedAdRepository, scrapperProperties);
        reloadApartments();
    }

    @Test
    public void there_is_13_listed_ads_on_page_3() {
        when(htmlProvider.adListing(any(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_3.getHtml()
        );

        Set<ListedAd> ads = scrapper.listing(katowice());

        Assert.assertEquals(13, ads.size());
    }

    @Test
    public void maximum_of_3_pages_of_apartments_will_be_scrapped_with_duplicated_ads_removed_from_listing() {
        // limit comes from ScrapperProperties
        when(htmlProvider.adListing(any(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_1.getHtml() // otherwise it will fall in the endless loop
        );

        Set<ListedAd> ads = scrapper.listing(katowice());

        Assert.assertEquals(13, ads.size());
    }

    @Test
    public void after_removing_already_saved_ads_from_page_3_there_will_be_12_listed_ads_to_be_scrapped() {
        when(htmlProvider.adListing(any(), anyInt())).thenReturn(
                HtmlFile.AD_LISTING_PAGE_3.getHtml()
        );

        Set<ListedAd> ads = scrapper.filteredListing(katowice());

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

        Set<Ad> ads = scrapper.scrapAds(katowice());

        // 3x 10 regular ads = 30 + 3 featured = 33
        // 1 ad on page 2 comes from page 1 = 33 - 1 = 32
        // last ad on page 3 already exist in db = 32 - 1 = 31
        Assert.assertEquals(31, ads.size());
    }

    private static City katowice() {
        return City.builder()
                .name("Katowice")
                .urlCode("v1c9008l3200285")
                .build();
    }
}
