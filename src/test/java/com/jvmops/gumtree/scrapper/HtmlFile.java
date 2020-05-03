package com.jvmops.gumtree.scrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Getter
@RequiredArgsConstructor
enum HtmlFile {
    AD_LISTING_SINGLE_AD("Katowice", toString(new ClassPathResource("html/ad_listing_single_ad.html"))),
    AD_LISTING_PAGE_1("Katowice", toString(new ClassPathResource("html/ad_listing_1.html"))),
    AD_LISTING_PAGE_2("Katowice", toString(new ClassPathResource("html/ad_listing_2_duplicates_at_start.html"))),
    AD_LISTING_PAGE_3("Katowice", toString(new ClassPathResource("html/ad_listing_3_duplicates_at_end.html"))),
    AD_DETAILS("Katowice", toString(new ClassPathResource("html/ad_details.html")));

    private final String city;
    private final String html;

    private static String toString(Resource resource) {
        try (var is = resource.getInputStream()) {
            return new Scanner(is, StandardCharsets.UTF_8.name())
                    .useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}