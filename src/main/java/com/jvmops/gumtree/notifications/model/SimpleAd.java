package com.jvmops.gumtree.notifications.model;

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

@Document("ad")
@Data
@EqualsAndHashCode(of = "id")
@Builder
public class SimpleAd {
    private ObjectId id;
    private String city;
    private Money price;
    private LocalDateTime creationTime;
    private LocalDateTime modificationTime;

    public String getCreationTimeAsString() {
        return creationTime.toLocalDate().toString();
    }
}
