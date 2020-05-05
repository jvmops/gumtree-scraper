package com.jvmops.gumtree.report;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
interface AdRepository extends CrudRepository<Ad, ObjectId> {
    List<Ad> findAllByCityAndCreationTimeGreaterThanOrderByPrice(String city, LocalDateTime yesterday);
    List<Ad> findByCityAndDescriptionContainsAndGumtreeCreationDateGreaterThan(String city, String description, LocalDate gumtreeCreationDate, Sort sort);
    List<Ad> findTop10ByCityAndGumtreeCreationDateGreaterThan(String city, LocalDate gumtreeCreationDate, Sort sort);
}
