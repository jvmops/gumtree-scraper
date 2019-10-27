package com.jvmops.gumtree.scrapper.ads;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

interface AdRepository extends CrudRepository<Ad, ObjectId> {
}
