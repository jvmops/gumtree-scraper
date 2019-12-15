package com.jvmops.gumtree.city;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
interface CityRepository extends CrudRepository<City, Long> {
    Optional<City> findByName(String city);
    Set<City> findAllByEmailsContaining(String email);
}
