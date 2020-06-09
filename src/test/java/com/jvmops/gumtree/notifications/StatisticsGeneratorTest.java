package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.subscriptions.model.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;

@SpringBootTest(classes = Main.class)
class StatisticsGeneratorTest extends JsonDataInitializer {
    private static final City KATOWICE = new City("Katowice");

    @Autowired StatisticsGenerator statisticsGenerator;

    // TODO: fabricate better data for this test
    @BeforeAll
    static void setup(@Autowired MongoTemplate mongoTemplate) {
        reload(mongoTemplate, DUMPED_ADS);
    }

    @Test
    public void new_ads_per_day_statistics_is_not_null() {
        var statistics = statisticsGenerator.getStatistics(KATOWICE);
        Assertions.assertNotNull(statistics.getNewPerDay());
    }

    @Test
    public void count_0_register_as_a_day_of_statistic() {
        var statistics = statisticsGenerator.getStatistics(KATOWICE);
        Assertions.assertEquals(0, statistics.getNewPerDay().get(LocalDate.parse("2020-05-02")));
    }

    @Test
    public void there_is_1_new_ad_posted_on_2020_05_05() {
        var statistics = statisticsGenerator.getStatistics(KATOWICE);
        Assertions.assertEquals(1, statistics.getNewPerDay().get(LocalDate.parse("2020-05-05")));
    }
}
