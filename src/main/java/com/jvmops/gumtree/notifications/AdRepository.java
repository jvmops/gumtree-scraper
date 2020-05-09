package com.jvmops.gumtree.notifications;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
interface AdRepository extends MongoRepository<Ad, ObjectId> {
    List<Ad> findTop20ByCityAndCreationTimeGreaterThanOrderByGumtreeCreationDate(String city, LocalDateTime time);
    List<Ad> findTop20ByCityAndPriceGreaterThanAndGumtreeCreationDateGreaterThan(
            String city,
            Integer price,
            LocalDate gumtreeCreationDate,
            Sort sort);
}
