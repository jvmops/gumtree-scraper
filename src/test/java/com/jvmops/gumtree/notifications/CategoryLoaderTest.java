package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.JsonDataInitializer;
import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.notifications.model.Category;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest(classes = Main.class)
class CategoryLoaderTest extends JsonDataInitializer {
    private static final String KATOWICE ="Katowice";
    private static final String WROCLAW ="Wroclaw";

    @Autowired
    private CheapestApartments cheapestApartments;
    @Autowired
    private NewestApartments newestApartments;
    @Autowired
    private DishwasherAndGasApartments dishwasherAndGasApartments;
    @Autowired
    private DishwasherOnlyApartments dishwasherOnlyApartments;

    @BeforeAll
    static void setup(@Autowired MongoTemplate mongoTemplate) {
        reloadReadOnlyAds(mongoTemplate, DUMPED_ADS);
    }

    @Test
    void category_is_empty_because_are_no_ads_for_wroclaw() {
        Category category = newestApartments.load(WROCLAW);
        Assert.assertTrue(category.isEmpty());
    }

    @Test
    void there_are_two_newest_apartments() {
        Category category = newestApartments.load(KATOWICE);
        Assert.assertEquals(2, category.getAds().size());
    }

    @Test
    void there_are_13_cheapest_apartments() {
        Category category = cheapestApartments.load(KATOWICE);
        Assert.assertEquals(8, category.getAds().size());
    }

    @Test
    void dishwasher_and_gas_apartments_exist() {
        Category category = dishwasherAndGasApartments.load(KATOWICE);
        Assert.assertEquals(3, category.getAds().size());
    }

    @Test
    void dishwasher_only_apartments_exist() {
        Category category = dishwasherOnlyApartments.load(KATOWICE);
        Assert.assertEquals(2, category.getAds().size());
    }
}
