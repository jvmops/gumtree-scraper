package com.jvmops.gumtree.notifications.ports;

import com.jvmops.gumtree.notifications.model.SimpleAd;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatisticsRepository extends MongoRepository<SimpleAd, ObjectId> {
    List<SimpleAd> findAllByCityAndCreationTimeGreaterThan(String city, LocalDateTime creationTime, Sort sort);
}
