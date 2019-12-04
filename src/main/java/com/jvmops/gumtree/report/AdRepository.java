package com.jvmops.gumtree.report;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Repository
public interface AdRepository extends CrudRepository<Ad, ObjectId> {
    List<Ad> findAllByCreationTimeGreaterThanAndRefreshedFalseOrderByPrice(LocalDateTime yesterday);
    List<Ad> findByDescriptionContains(String description);
    List<Ad> findTop10ByGumtreeCreationDateGreaterThan(LocalDate gumtreeCreationDate, Sort sort);
}
