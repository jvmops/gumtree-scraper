package com.jvmops.gumtree.scrapper;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ScrappedAdRepository extends CrudRepository<Ad, ObjectId> {
    Optional<Ad> findByCityAndTitle(String city, String title);
}
