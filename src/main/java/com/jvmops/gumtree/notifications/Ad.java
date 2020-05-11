package com.jvmops.gumtree.notifications;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
@EqualsAndHashCode(of = "id")
@Builder
class Ad {
    @Id
    private ObjectId id;
    private String city;
    private URL url;
    @Indexed
    private String title;
    @Indexed
    private String description;
    private Money price;
    private LocalDate availableSince;
    private String landlord;
    private Integer size;
    @Indexed
    private LocalDate gumtreeCreationDate;
    private List<LocalDate> updates;
    private LocalDateTime creationTime;
}
