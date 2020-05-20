package com.jvmops.gumtree.subscriptions.ports;

import com.jvmops.gumtree.subscriptions.model.City;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends MongoRepository<City, Long> {
    Optional<City> findByNameIgnoreCase(String city);
}
