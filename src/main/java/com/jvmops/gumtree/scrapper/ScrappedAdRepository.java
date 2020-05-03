package com.jvmops.gumtree.scrapper;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
interface ScrappedAdRepository extends CrudRepository<Ad, ObjectId> {
    Optional<Ad> findByCityAndTitle(String city, String title);
    @ExistsQuery(value = "{ 'gumtreeId' : {$in : [?0] }}")
    boolean existsByGumtreeIds(Set<String> gumtreeIds);
}
