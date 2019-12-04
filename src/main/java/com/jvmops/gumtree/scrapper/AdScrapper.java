package com.jvmops.gumtree.scrapper;

import java.util.List;

interface AdScrapper {
    List<Ad> scrapAds(String url);
}
