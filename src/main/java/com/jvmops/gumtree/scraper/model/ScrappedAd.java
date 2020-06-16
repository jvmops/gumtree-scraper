package com.jvmops.gumtree.scraper.model;

import com.jvmops.gumtree.scraper.PriceChange;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.joda.money.Money;
import org.springframework.data.annotation.CreatedDate;
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
@Getter
@Setter
@Builder
public class ScrappedAd {
    private ObjectId id;
    private String gumtreeId;
    private String city;
    private URL url;
    private boolean featured;
    @Indexed
    private String title;
    private Money price;
    @Indexed
    private String description;
    private LocalDate availableSince;
    private String landlord;
    private Integer size;
    @Indexed
    private LocalDate gumtreeCreationDate;
    private Set<LocalDate> seenOn;

    @CreatedDate
    @Indexed
    private LocalDateTime creationTime;
    @LastModifiedDate
    private LocalDateTime modificationTime;
}
