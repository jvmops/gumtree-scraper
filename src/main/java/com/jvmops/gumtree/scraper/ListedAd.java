package com.jvmops.gumtree.scraper;

import lombok.*;
import org.joda.money.Money;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ad")
@Getter
@Setter
@Data
@EqualsAndHashCode(of = "gumtreeId")
@Builder
class ListedAd {
    private String city;
    private String gumtreeId;
    private String url;
    private String title;
    private Money price;
    private boolean featured;
}
