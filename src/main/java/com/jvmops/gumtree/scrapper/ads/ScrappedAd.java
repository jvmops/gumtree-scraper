package com.jvmops.gumtree.scrapper.ads;

import lombok.Builder;
import lombok.Getter;
import org.openqa.selenium.WebElement;

@Getter
@Builder
class ScrappedAd {
    private AdSummary adSummary;
    private WebElement ad;
}
