package com.jvmops.gumtree.scraper;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ad")
@Getter
@Setter
@EqualsAndHashCode(of = "gumtreeId")
@Builder
class ListedAd {
    private String city;
    private String gumtreeId;
    private String url;
    private String title;
    private Integer price;
    private boolean featured;
}
