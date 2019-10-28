package com.jvmops.gumtree.scrapper;

import lombok.Builder;
import lombok.Getter;
import org.openqa.selenium.WebElement;

@Getter
@Builder
class ScrappedAd {
    private ScrappedAdSummary scrappedAdSummary;
    private WebElement ad;
}
