package com.jvmops.gumtree.city;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private Set<String> emails;
    @CreatedDate
    private LocalDateTime creationTime;
    @LastModifiedDate
    private LocalDateTime modificationTime;

    public City(String name) {
        this.name = name;
    }

    public List<String> getEmailsAsList() {
        return new ArrayList<>(emails);
    }

    public boolean subscribe(String email) {
        return emails.add(email);
    }
}