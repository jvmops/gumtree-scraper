package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.HtmlFile;
import com.jvmops.gumtree.scraper.Slowdown;
import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.model.ScrappedAd;
import org.joda.money.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.jvmops.gumtree.ScrapperConfig.DEFAULT_CURRENCY;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdDetailsScrapperTest {

    @Mock
    private HtmlProvider htmlProvider;
    @Mock
    private Slowdown slowdown;
    private JSoupAdDetailsScrapper adDetailsScrapper;

    @BeforeEach
    void initializeDependencies() {
        adDetailsScrapper = new JSoupAdDetailsScrapper(htmlProvider, slowdown);
    }

    @Test
    void ad_details_can_be_scrapped_from_the_listing() {
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        ScrappedAd adDetails = scrap(listedAd());

        // those comes from adListing
        Assertions.assertEquals("Katowice", adDetails.getCity());
        Assertions.assertEquals("Fajne mieszkanie do wynajecia", adDetails.getTitle());
        Assertions.assertEquals(GumtreeUrlBuilder.parseUrl("https://www.gumtree.pl/a-mieszkania-i-domy-do-wynajecia/katowice/0195021950192"), adDetails.getUrl());
        Assertions.assertEquals("0195021950192", adDetails.getGumtreeId());
        Assertions.assertEquals(3300, adDetails.getPrice().getAmountMajorInt());
        Assertions.assertTrue(adDetails.isFeatured());
    }

    @Test
    void description_is_scrapped() {
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        ScrappedAd adDetails = scrap(listedAd());

        Assertions.assertTrue(adDetails.getDescription().length() > 500);
    }

    @Test
    void landlord_is_scrapped() {
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        ScrappedAd adDetails = scrap(listedAd());

        Assertions.assertEquals("Agencja", adDetails.getLandlord());
    }

    @Test
    void gumtree_creation_date_is_parsed() {
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        ScrappedAd adDetails = scrap(listedAd());

        Assertions.assertEquals(LocalDate.parse("2019-11-10"), adDetails.getGumtreeCreationDate());
    }

    @Test
    void size_is_parsed() {
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        ScrappedAd adDetails = scrap(listedAd());

        Assertions.assertEquals(32, adDetails.getSize());
    }

    @Test
    void not_present_attribute_available_since_will_result_in_null() {
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        ScrappedAd adDetails = scrap(listedAd());

        Assertions.assertNull(adDetails.getAvailableSince());
    }

    private void setupHtmlMockFor(HtmlFile htmlFile) {
        when(htmlProvider.adDetails(Mockito.any()))
                .thenReturn(htmlFile.getHtml());
    }

    private ScrappedAd scrap(ListedAd listedAd) {
        return adDetailsScrapper.scrap(listedAd)
                .orElseThrow(() -> new IllegalArgumentException("Ad details scrapping failed"));
    }

    private static ListedAd listedAd() {
        return ListedAd.builder()
                .city("Katowice")
                .gumtreeId("0195021950192")
                .price(Money.of(DEFAULT_CURRENCY, 3300))
                .featured(true)
                .title("Fajne mieszkanie do wynajecia")
                .url(GumtreeUrlBuilder.parseUrl("https://www.gumtree.pl/a-mieszkania-i-domy-do-wynajecia/katowice/0195021950192"))
                .build();
    }
}
