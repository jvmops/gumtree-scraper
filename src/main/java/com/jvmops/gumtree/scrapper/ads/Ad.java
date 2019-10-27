package com.jvmops.gumtree.scrapper.ads;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
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
    private String title;
    private String description;
    private Integer price;
    private LocalDate availableSince;
    private String landlord;
    private Integer size;
    private LocalDate creationDate;
    private List<LocalDate> updates;
}
