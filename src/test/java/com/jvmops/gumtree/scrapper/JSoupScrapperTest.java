package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static com.jvmops.gumtree.scrapper.ScrapJob.GUMTREE_URL;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JSoupScrapperTest {
    @Mock
    private HtmlProvider htmlProvider;
    private JSoupAdListingParser JSoupAdListingParser;
    private JSoupAdParser JSoupAdParser;
    private AdScrapper adScrapper;

    @BeforeEach
    void initializeDependencies() {
        JSoupAdListingParser = new JSoupAdListingParser(htmlProvider);
        JSoupAdParser = new JSoupAdParser(htmlProvider);
        adScrapper = new JSoupScrapper(JSoupAdListingParser, JSoupAdParser);
    }

    @Test
    void ad_listing_html_file_contain_20_ads() {
        setupHtmlMockFor(HtmlFile.AD_LISTING);

        List<ListedAd> ads = JSoupAdListingParser.scrap(HtmlFile.AD_LISTING.getUrl());

        Assertions.assertEquals(20, ads.size());
    }

    @Test
    void ad_details_can_be_scrapped_from_a_listed_ad_url() {
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        ListedAd listedAd = ListedAd.builder()
                .url(HtmlFile.AD_DETAILS.getUrl())
                .build();
        Ad ad = JSoupAdParser.scrap(listedAd);

        Assertions.assertEquals(LocalDate.parse("2019-11-10"), ad.getGumtreeCreationDate());
        Assertions.assertEquals(1, ad.getUpdates().size());
    }

    @Test
    void domain_ad_objects_will_be_scrapped_from_an_ad_listing_url() {
        setupHtmlMockFor(HtmlFile.AD_LISTING);
        setupHtmlMockFor(HtmlFile.AD_DETAILS);

        List<Ad> ads = adScrapper.scrapAds(HtmlFile.AD_LISTING.getUrl());

        Assertions.assertEquals(20, ads.size());
    }

    private void setupHtmlMockFor(HtmlFile htmlFile) {
        when(htmlProvider.get(htmlFile.getUrl()))
                .thenReturn(htmlFile.getHtml());
    }

}

@Getter
@RequiredArgsConstructor
enum HtmlFile {
    AD_LISTING(GUMTREE_URL + "/ad-listing", toString(new ClassPathResource("html/ad_listing.html"))),
    AD_DETAILS(GUMTREE_URL + "/ad-details", toString(new ClassPathResource("html/ad_details.html")));

    private final String url;
    private final String html;

    private static String toString(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            return new Scanner(is, StandardCharsets.UTF_8.name())
                    .useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}