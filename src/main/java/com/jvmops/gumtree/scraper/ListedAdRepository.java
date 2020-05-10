package com.jvmops.gumtree.scraper;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
interface ListedAdRepository extends MongoRepository<ListedAd, ObjectId> {
    Set<ListedAd> findByGumtreeIdIn(Set<String> gumtreeIds);
    boolean existsByGumtreeIdIn(Set<String> gumtreeIds);
}
