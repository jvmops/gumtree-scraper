package com.jvmops.gumtree.acceptance;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.notifications.JsonDataInitializer;
import com.jvmops.gumtree.notifications.model.Ad;
import com.jvmops.gumtree.notifications.ports.ShameRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * In before {@link ShameRepository}
 */
@SpringBootTest(classes = Main.class)
class MongoRegexpQueryAcceptanceTest extends JsonDataInitializer {
    @Autowired
    private MongoAcceptanceTestRepository repository;

    @BeforeAll
    static void setup(@Autowired MongoTemplate mongoTemplate) {
        reload(mongoTemplate, ACCEPTANCE_TEST_ADS);
    }

    @Test
    void there_are_two_dishwasher_and_gas_apartment() {
        List<Ad> ads = repository.findAllByRegexpOverDescription("((zmywark.*)(gaz.*))|((gaz.*)(zmywark.*))");
        Assert.assertEquals(2, ads.size());
    }

    @Test
    void there_are_three_apartments_with_dishwasher() {
        List<Ad> ads = repository.findAllByRegexpOverDescription("zmywark");
        Assert.assertEquals(3, ads.size());
    }

    @Test
    void there_is_one_dishwasher_only_apartment() {
        List<Ad> ads = repository.findAllByRegexpInDescriptionAndRegexpNotInDescription("zmywark", "^((?!gaz).)*$");
        Assert.assertEquals(1, ads.size());
    }
}

