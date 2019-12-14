package com.jvmops.gumtree.report;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@Builder
class Ad {
    private ObjectId id;
    private String city;
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
    private LocalDate gumtreeCreationDate;
    private List<LocalDate> updates;
    private LocalDateTime creationTime;

    void setGumtreeCreationDate(LocalDate gumtreeCreationDate) {
        this.gumtreeCreationDate = gumtreeCreationDate;
        this.updates.add(gumtreeCreationDate);
    }
}
