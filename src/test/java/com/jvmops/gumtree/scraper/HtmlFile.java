package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.FileReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
enum HtmlFile {
    AD_LISTING_EMPTY_PAGE("Katowice", FileReader.toString("html/ad_listing_empty.html")),
    AD_LISTING_SINGLE_AD("Katowice", FileReader.toString("html/ad_listing_single_ad.html")),
    AD_LISTING_PAGE_1("Katowice", FileReader.toString("html/ad_listing_1.html")),
    AD_LISTING_PAGE_2("Katowice", FileReader.toString("html/ad_listing_2_duplicates_at_start.html")),
    AD_LISTING_PAGE_3("Katowice", FileReader.toString("html/ad_listing_3_duplicates_at_end.html")),
    AD_DETAILS("Katowice", FileReader.toString("html/ad_details.html"));

    private final String city;
    private final String html;


}