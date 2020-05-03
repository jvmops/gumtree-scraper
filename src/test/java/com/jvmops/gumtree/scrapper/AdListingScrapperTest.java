package com.jvmops.gumtree.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdListingScrapperTest {

    @Mock
    private HtmlProvider htmlProvider;
    private JSoupAdListingScrapper adListingScrapper;

    @BeforeEach
    void initializeDependencies() {
        var adUrlBuilder = new AdUrlBuilder();
        adListingScrapper = new JSoupAdListingScrapper(htmlProvider, adUrlBuilder);
    }

    @Test
    void page_one_contains_3_featured_ads() {
        setupHtmlMockFor(HtmlFile.AD_LISTING_PAGE_1);

        List<ListedAd> ads = adListingScrapper.scrap("Katowice", 1).stream()
                .filter(ListedAd::isFeatured)
                .collect(Collectors.toList());

        Assertions.assertEquals(3, ads.size());
    }

    @Test
    void page_one_contains_10_regular_ads() {
        setupHtmlMockFor(HtmlFile.AD_LISTING_PAGE_1);

        List<ListedAd> ads = adListingScrapper.scrap("Katowice", 1).stream()
                .filter(Predicate.not(ListedAd::isFeatured))
                .collect(Collectors.toList());

        Assertions.assertEquals(10, ads.size());
    }

    @Test
    void city_is_scrapped_from_the_listing() {
        setupHtmlMockFor(HtmlFile.AD_LISTING_SINGLE_AD);

        ListedAd listedAd = adListingScrapper.scrap("Katowice", 1)
                .get(0);

        Assertions.assertEquals("Katowice", listedAd.getCity());
    }

    @Test
    void gumtree_id_is_scrapped_from_the_listing() {
        setupHtmlMockFor(HtmlFile.AD_LISTING_SINGLE_AD);

        ListedAd listedAd = adListingScrapper.scrap("Katowice", 1)
                .get(0);

        Assertions.assertEquals("1007246940240911148463809", listedAd.getGumtreeId());
    }

    @Test
    void title_is_scrapped_from_the_listing() {
        setupHtmlMockFor(HtmlFile.AD_LISTING_SINGLE_AD);

        ListedAd listedAd = adListingScrapper.scrap("Katowice", 1)
                .get(0);

        Assertions.assertEquals("Apartament trzypokojowy na przedmieściach Katowic", listedAd.getTitle());
    }

    @Test
    void url_is_scrapped_from_the_listing() {
        setupHtmlMockFor(HtmlFile.AD_LISTING_SINGLE_AD);

        ListedAd listedAd = adListingScrapper.scrap("Katowice", 1)
                .get(0);

        Assertions.assertEquals("https://www.gumtree.pl/a-mieszkania-i-domy-do-wynajecia/katowice/1007246940240911148463809", listedAd.getUrl());
    }



    @Test
    void price_is_parsed_from_the_listing() {
        setupHtmlMockFor(HtmlFile.AD_LISTING_SINGLE_AD);

        ListedAd listedAd = adListingScrapper.scrap("Katowice", 1)
                .get(0);

        Assertions.assertEquals(2490, listedAd.getPrice());
    }

    private void setupHtmlMockFor(HtmlFile htmlFile) {
        when(htmlProvider.adListing("Katowice", 1))
                .thenReturn(htmlFile.getHtml());
    }
}
