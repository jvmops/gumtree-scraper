package com.jvmops.gumtree.scrapper;

import java.util.List;

public interface AdScrapper {
    List<Ad> scrapAds(String url);
}
