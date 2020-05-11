package com.jvmops.gumtree.scraper.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.joda.money.Money;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;

@Document(collection = "ad")
@Value
@EqualsAndHashCode(of = "gumtreeId")
@Builder
public class ListedAd {
    String city;
    String gumtreeId;
    URL url;
    String title;
    Money price;
    boolean featured;
}
