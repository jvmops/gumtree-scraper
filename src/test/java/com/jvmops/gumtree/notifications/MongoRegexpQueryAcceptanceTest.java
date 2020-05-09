package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * In before {@link SuperQuerasyK}
 */
@SpringBootTest(classes = Main.class)
class MongoRegexpQueryAcceptanceTest extends JsonDataInitializer {
    @Autowired
    private AcceptanceTestRepository repository;

    @BeforeAll
    static void setup(@Autowired MongoTemplate mongoTemplate) {
        reloadAds(mongoTemplate, ACCEPTANCE_TEST_ADS);
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

@Repository
interface AcceptanceTestRepository extends MongoRepository<Ad, ObjectId> {
    @Query(value = """
            {
                description: {
                    $regex:?0 
                }
            }""")
    List<Ad> findAllByRegexpOverDescription(String inDescription);
    @Query(value = """
            { $and: 
                [{
                    description: {
                        $regex:?0
                    }
                },
                {
                    description: {
                        $regex:?1
                    }
                }]
            }""")
    List<Ad> findAllByRegexpInDescriptionAndRegexpNotInDescription(String inDescription, String notInDescription);
}
