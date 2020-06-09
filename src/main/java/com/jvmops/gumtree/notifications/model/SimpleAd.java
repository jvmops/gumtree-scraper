package com.jvmops.gumtree.notifications.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.joda.money.Money;
import org.springframework.data.mongodb.core.mapping.Document;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Document("ad")
@Data
@EqualsAndHashCode(of = "id")
@Builder
public class SimpleAd {
    private ObjectId id;
    private String city;
    private Money price;
    private LocalDate gumtreeCreationDate;
    private LocalDateTime creationTime;
    private LocalDateTime modificationTime;
    private Set<LocalDate> seenOn;

    @Transient
    public LocalDate getLastSeenOn() {
        return modificationTime.toLocalDate();
    }

    @Transient
    public boolean wasSeenOn(LocalDate date) {
        return seenOn.contains(date);
    }
}
