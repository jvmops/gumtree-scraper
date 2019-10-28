package com.jvmops.gumtree.scrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document
@Getter
@Setter
@Builder
class Ad {
    private ObjectId id;
    private String url;
    @Indexed
    private String title;
    @TextIndexed
    private String description;
    private Integer price;
    private LocalDate availableSince;
    private String landlord;
    private Integer size;
    @Indexed
    private LocalDate creationDate;
    private List<LocalDate> updates;

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        this.updates.add(creationDate);
    }
}
