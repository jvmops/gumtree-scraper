package com.jvmops.gumtree;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;

import java.util.stream.Stream;

/**
 * Base class for Mongo tests. With this we can use single container for all tests.
 */
@Slf4j
public abstract class MongoTest {
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            log.info("Updating spring context with mongo testcontainer port");
            log.info("MONGO.getFirstMappedPort() == {}", MONGO.getFirstMappedPort());
            TestPropertyValues.of(
                    "spring.data.mongodb.port=" + MONGO.getFirstMappedPort()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    public static final GenericContainer MONGO = new GenericContainer("mongo:4")
            .withExposedPorts(27017);

    public static final MongoClient MONGO_CLIENT;

    static {
        log.info("::: STARTING CONTAINER :::");
        MONGO.start();
        log.info("::: CONTAINER STARTED :::");

        log.info(":: Initializing static MongoClient ::");
        // for direct access to the test Mongo
        MONGO_CLIENT = new MongoClient("localhost", MONGO.getFirstMappedPort());
        log.info(":: static MongoClient initialized ::");
    }

    // just for refecerence on how to use MongoClient directly
    protected static void deleteAll() {
        BasicDBObject all = new BasicDBObject();
        mongoCollectionNames()
                .peek(collectionName -> log.info("Removing all the data from: {}", collectionName))
                .peek(collectionName -> log.info("Collection {} count: {}", collectionName, count(collectionName)))
                .map(MongoTest::getMongoCollection)
                .forEach(collection -> collection.deleteMany(all));
    }

    @NotNull
    private static Stream<String> mongoCollectionNames() {
        return Stream.of("ad", "mailConfig");
    }

    private static long countAll() {
        return mongoCollectionNames()
                .map(MongoTest::getMongoCollection)
                .map(MongoCollection::countDocuments)
                .count();
    }

    private static long count(String collectionName) {
        return getMongoCollection(collectionName)
                .countDocuments();
    }

    private static MongoCollection<Document> getMongoCollection(String collectionName) {
        var mongo = MONGO_CLIENT.getDatabase("test-gumtree");
        return mongo.getCollection(collectionName);
    }
}
