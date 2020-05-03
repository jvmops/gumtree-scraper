package com.jvmops.gumtree.scrapper;

import java.util.List;

interface AdScrapper {
    List<ListedAd> scrapListing(String city, int pageNumber);
    Ad scrapAd(ListedAd listedAd);
}
