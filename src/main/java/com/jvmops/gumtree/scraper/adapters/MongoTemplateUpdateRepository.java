package com.jvmops.gumtree.scraper.adapters;

import com.jvmops.gumtree.scraper.model.ListedAd;
import com.jvmops.gumtree.scraper.ports.UpdateRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
@AllArgsConstructor
public class MongoTemplateUpdateRepository implements UpdateRepository {
    private MongoTemplate mongoTemplate;

    @Override
    public void updateSeenOn(Set<ObjectId> ids, LocalDate date) {
        mongoTemplate.updateMulti(
                new Query(Criteria.where("id").in(ids)),
                new Update().addToSet("seenOn", date),
                ListedAd.class
        );
    }
}
