package com.jvmops.gumtree.notifications.ports;

import com.jvmops.gumtree.notifications.model.Ad;
import org.bson.types.ObjectId;
import org.joda.money.Money;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdRepository extends MongoRepository<Ad, ObjectId> {
    List<Ad> findTop20ByCityAndCreationTimeGreaterThanOrderByGumtreeCreationDate(
            String city,
            LocalDateTime time);
    List<Ad> findTop20ByCityAndPriceGreaterThanAndGumtreeCreationDateGreaterThan(
            String city,
            Money price,
            LocalDate gumtreeCreationDate,
            Sort sort);
}
