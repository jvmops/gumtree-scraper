package com.jvmops.gumtree.scrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JSoupScrapperTest {
    @Mock
    private HtmlProvider htmlProvider;
    private JSoupAdListingScrapper adListingParser;
    private JSoupAdDetailsScrapper adParser;
    private AdScrapper scrapper;

    @BeforeEach
    void initializeDependencies() {
        adListingParser = new JSoupAdListingScrapper(htmlProvider);
        adParser = new JSoupAdDetailsScrapper(htmlProvider);
        scrapper = new JSoupScrapper(adListingParser, adParser);
    }
//
//    @Test
//    void page_two_does_not_contain_duplicates_at_the_end_of_the_list() {
//        setupHtmlMockFor(HtmlFile.AD_LISTING_PAGE_1);
//
//        List<ListedAd> ads = scrapper.scrapListing(HtmlFile.AD_LISTING.getCity(), 1).stream()
//                .filter(Predicate.not(ListedAd::isFeatured))
//                .collect(Collectors.toList());
//
//        Assertions.assertEquals(10, ads.size());
//    }
//
//    @Test
//    void ad_details_can_be_scrapped_from_a_listed_ad_url() {
//        setupHtmlMockFor(HtmlFile.AD_DETAILS);
//
//        ListedAd listedAd = ListedAd.builder()
//                .url(HtmlFile.AD_DETAILS.getCity())
//                .build();
//        Ad ad = adParser.scrap(listedAd);
//
//        Assertions.assertEquals(LocalDate.parse("2019-11-10"), ad.getGumtreeCreationDate());
//        Assertions.assertEquals(1, ad.getUpdates().size());
//    }
//
//    @Test
//    void domain_ad_objects_will_be_scrapped_from_an_ad_listing_url() {
//        setupHtmlMockFor(HtmlFile.AD_LISTING);
//        setupHtmlMockFor(HtmlFile.AD_DETAILS);
//
//        List<Ad> ads = scrapper.scrapAd(HtmlFile.AD_LISTING.getCity());
//
//        Assertions.assertEquals(20, ads.size());
//    }
//
//    private void setupHtmlMockFor(HtmlFile htmlFile) {
//        when(htmlProvider.get(htmlFile.getCity()))
//                .thenReturn(htmlFile.getHtml());
//    }

}
