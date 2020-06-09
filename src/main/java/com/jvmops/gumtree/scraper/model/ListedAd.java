package com.jvmops.gumtree.scraper.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Document("ad")
@CompoundIndex(name = "city_title", def = "{'city' : 1, 'title': 1}", unique = true)
@Data
@EqualsAndHashCode(of = "gumtreeId")
@Builder
public class ListedAd {
    @Id
    private ObjectId id;
    @Indexed
    private String city;
    private String gumtreeId;
    private URL url;
    @Indexed
    private String title;
    private Money price;
    private boolean featured;
    private Set<LocalDate> seenOn;
    @LastModifiedDate
    private LocalDateTime modificationTime;

    public void seenOn(LocalDate date) {
        seenOn.add(date);
    }
}
