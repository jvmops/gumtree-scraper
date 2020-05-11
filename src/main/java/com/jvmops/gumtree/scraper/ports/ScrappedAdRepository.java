package com.jvmops.gumtree.scraper.ports;

import com.jvmops.gumtree.scraper.model.ScrappedAd;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ScrappedAdRepository extends CrudRepository<ScrappedAd, ObjectId> {
    Optional<ScrappedAd> findByCityAndTitle(String city, String title);
    @ExistsQuery(value = "{ 'gumtreeId' : {$in : [?0] }}")
    boolean existsByGumtreeIds(Set<String> gumtreeIds);
}
