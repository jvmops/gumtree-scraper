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
            TestPropertyValues.of(
                    "spring.data.mongodb.port=" + MONGO.getFirstMappedPort()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    public static final GenericContainer MONGO = new GenericContainer("mongo:3.4.23-xenial")
            .withExposedPorts(27017);

    public static final MongoClient MONGO_CLIENT;

    static {
        MONGO.start();
        MONGO_CLIENT = new MongoClient("localhost", MONGO.getFirstMappedPort());
    }

    public static void deleteAll() {
        BasicDBObject all = new BasicDBObject();
        log.info("Removing all the data from [ gumtree.ad ] collection");
        MONGO_CLIENT.getDatabase("test-gumtree")
                .getCollection("ad")
                .deleteMany(all);
    }
}
