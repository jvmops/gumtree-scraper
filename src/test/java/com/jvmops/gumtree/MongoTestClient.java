package com.jvmops.gumtree;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;

import java.util.stream.Stream;

/**
 * Created just for reference
 */
@Component
@Slf4j
public class MongoTestClient {

    private MongoClient mongoClient;

    public MongoTestClient(GenericContainer mongodb) {
        this.mongoClient = new MongoClient(
                mongodb.getContainerIpAddress(),
                mongodb.getFirstMappedPort()
        );
    }

    // just for refecerence on how to use MongoClient directly
    public void deleteAll() {
        BasicDBObject all = new BasicDBObject();
        mongoCollectionNames()
                .peek(collectionName -> log.info("Removing all the data from: {}", collectionName))
                .peek(collectionName -> log.info("Collection {} count: {}", collectionName, count(collectionName)))
                .map(this::getMongoCollection)
                .forEach(collection -> collection.deleteMany(all));
    }

    private Stream<String> mongoCollectionNames() {
        return Stream.of("ad", "city");
    }

    private long countAll() {
        return mongoCollectionNames()
                .map(this::getMongoCollection)
                .map(MongoCollection::countDocuments)
                .count();
    }

    private long count(String collectionName) {
        return getMongoCollection(collectionName)
                .countDocuments();
    }

    private MongoCollection<Document> getMongoCollection(String collectionName) {
        var mongo = mongoClient.getDatabase("test-gumtree");
        return mongo.getCollection(collectionName);
    }
}
