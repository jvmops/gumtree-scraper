package com.jvmops.gumtree;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;

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
        log.info("Removing all the data from [ test-gumtree.ad ] collection");
        MONGO_CLIENT.getDatabase("test-gumtree")
                .getCollection("ad")
                .deleteMany(all);
    }
}
