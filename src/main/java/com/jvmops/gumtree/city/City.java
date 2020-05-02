package com.jvmops.gumtree.city;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    @Builder.Default
    private Set<String> notifications = new HashSet<>();
    @CreatedDate
    private LocalDateTime creationTime;
    @LastModifiedDate
    private LocalDateTime modificationTime;

    public City(String name) {
        this.name = name;
    }

    public boolean subscribe(String email) {
        return notifications.add(email);
    }
}
