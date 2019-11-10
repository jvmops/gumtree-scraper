package com.jvmops.gumtree.scrapper;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface AdRepository extends CrudRepository<Ad, ObjectId> {
    Optional<Ad> findByTitle(String title);
    List<Ad> findAllByCreationTimeGreaterThanAndRefreshedFalseOrderByPrice(LocalDateTime yesterday);
    List<Ad> findByDescriptionContains(String description);
    List<Ad> findTop10ByGumtreeCreationDateGreaterThan(LocalDate gumtreeCreationDate, Sort sort);
}
