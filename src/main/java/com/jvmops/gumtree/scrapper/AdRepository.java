package com.jvmops.gumtree.scrapper;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AdRepository extends CrudRepository<Ad, ObjectId> {
    Ad findByTitle(String title);
}
