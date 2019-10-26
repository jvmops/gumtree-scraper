package com.jvmops.gumtree.scrapper.ads;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
class AdSummary {
    private String url;
    private String title;
    private Integer price;
}
