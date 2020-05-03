package com.jvmops.gumtree.scrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document
@CompoundIndex(name = "city_title", def = "{'city' : 1, 'title': 1}", unique = true)
@Getter
@Setter
@Builder
class Ad {
    private ObjectId id;
    private String gumtreeId;
    private String city;
    private String url;
    private boolean featured;
    @Indexed
    private String title;
    private Integer price;
    @TextIndexed
    private String description;
    private LocalDate availableSince;
    private String landlord;
    private Integer size;
    @Indexed
    private LocalDate gumtreeCreationDate;
    private List<LocalDate> updates;
    @CreatedDate
    private LocalDateTime creationTime;
    @LastModifiedDate
    private LocalDateTime modificationTime;

    void setGumtreeCreationDate(LocalDate gumtreeCreationDate) {
        this.gumtreeCreationDate = gumtreeCreationDate;
        this.updates.add(gumtreeCreationDate);
    }
}
