package com.jvmops.gumtree.scraper.ports;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Set;

public interface UpdateRepository {
    void updateSeenOn(Set<ObjectId> ids, LocalDate date);
}
