package com.jvmops.gumtree.notifications;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
interface AdRepository extends CrudRepository<Ad, ObjectId> {
    List<Ad> findTop20ByCityAndCreationTimeGreaterThanOrderByGumtreeCreationDate(String city, LocalDateTime yesterday);
    List<Ad> findByCityAndDescriptionContainsAndGumtreeCreationDateGreaterThan(String city, String description, LocalDate gumtreeCreationDate, Sort sort);
    List<Ad> findTop20ByCityAndPriceGreaterThanAndGumtreeCreationDateGreaterThan(String city, Integer price, LocalDate gumtreeCreationDate, Sort sort);
}
