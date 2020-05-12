package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.adapters.GumtreeUrlBuilder.AdUrl;
import com.jvmops.gumtree.subscriptions.model.City;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

public class GumtreeUrlBuilderTest {
    GumtreeUrlBuilder gumtreeUrlBuilder = new GumtreeUrlBuilder();

    @Test
    void if_url_is_malformed_illegal_argument_exception_will_be_thrown() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> GumtreeUrlBuilder.parseUrl("htps://www.gumtree.pl/s-mieszkania-i-domy-do-wynajecia/v1c9008l3200285p1")
        );
    }

    @Test
    void ad_listing_url_will_be_created_from_city_code() {
        URL url = gumtreeUrlBuilder.adListingUrl("v1c9008l3200285");
        Assert.assertEquals(
                GumtreeUrlBuilder.parseUrl("https://www.gumtree.pl/s-mieszkania-i-domy-do-wynajecia/v1c9008l3200285p1"),
                url);
    }

    @Test
    void ad_listing_url_will_be_created_from_city_and_page_number() {
        City city = City.builder()
                .code("v1c9008l3200285")
                .build();
        URL url = gumtreeUrlBuilder.adListingUrl(city, 1);

        Assert.assertEquals(
                GumtreeUrlBuilder.parseUrl("https://www.gumtree.pl/s-mieszkania-i-domy-do-wynajecia/v1c9008l3200285p1"),
                url);
    }

    @Test
    void gumtree_id_can_be_scrapped_from_url_suffix() {
        AdUrl adUrl = gumtreeUrlBuilder.buildAdUrl("/a-mieszkania-i-domy-do-wynajecia/katowice/1007246940240911148463809");
        Assert.assertEquals(
                "1007246940240911148463809",
                adUrl.gumtreeId());
    }

    @Test
    void full_ad_URL_from_suffix_is_created() {
        AdUrl adUrl = gumtreeUrlBuilder.buildAdUrl("/a-mieszkania-i-domy-do-wynajecia/katowice/1007246940240911148463809");
        Assert.assertEquals(
                GumtreeUrlBuilder.parseUrl("https://www.gumtree.pl/a-mieszkania-i-domy-do-wynajecia/1007246940240911148463809"),
                adUrl.url());
    }
}
