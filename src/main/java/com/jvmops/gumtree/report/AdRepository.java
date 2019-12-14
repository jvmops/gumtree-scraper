package com.jvmops.gumtree.report;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
interface AdRepository extends CrudRepository<Ad, ObjectId> {
    List<Ad> findAllByCityAndCreationTimeGreaterThanAndRefreshedFalseOrderByPrice(String city, LocalDateTime yesterday);
    List<Ad> findByCityAndDescriptionContains(String city, String description);
    List<Ad> findTop10ByCityAndGumtreeCreationDateGreaterThan(String city, LocalDate gumtreeCreationDate, Sort sort);
}
