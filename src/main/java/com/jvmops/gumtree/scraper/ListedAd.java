package com.jvmops.gumtree.scraper;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.joda.money.Money;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ad")
@Value
@EqualsAndHashCode(of = "gumtreeId")
@Builder
class ListedAd {
    String city;
    String gumtreeId;
    String url;
    String title;
    Money price;
    boolean featured;
}
