package com.jvmops.gumtree.scrapper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListedAd {
    private String url;
    private String title;
    private Integer price;
}
