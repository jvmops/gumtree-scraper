package com.jvmops.gumtree.scrapper;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

interface ListedAdRepository extends MongoRepository<ListedAd, ObjectId> {
    Set<ListedAd> findAllByGumtreeId(Set<String> gumtreeIds);
}
