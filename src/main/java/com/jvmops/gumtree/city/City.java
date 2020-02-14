package com.jvmops.gumtree.city;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document
@Setter
@Getter
@Builder
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    private Set<String> notifications;
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
