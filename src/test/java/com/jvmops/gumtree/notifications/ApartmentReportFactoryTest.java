package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.subscriptions.City;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = Main.class)
class ApartmentReportFactoryTest extends JsonDataInitializer {

    private static final City KATOWICE = new City("Katowice");
    private static final City WROCLAW = new City("Wroclaw");

    @Autowired
    private ApartmentReportFactory apartmentReportFactory;

    @BeforeAll
    static void setup(@Autowired MongoTemplate mongoTemplate) {
        reloadAds(mongoTemplate, DUMPED_ADS);
    }

    @Test
    void report_is_empty_because_there_are_no_ads_for_wroclaw() {
        ApartmentReport apartmentReport = apartmentReportFactory.create(WROCLAW, ReportType.DAILY);
        assertTrue(apartmentReport.isEmpty());
    }

    @Test
    void all_the_categories_are_used_for_initial_report() {
        ApartmentReport apartmentReport = apartmentReportFactory.create(KATOWICE, ReportType.INITIAL);
        assertEquals(4, apartmentReport.getCategories().size());
    }

    @Test
    void there_is_only_one_category_for_newest_report() {
        ApartmentReport apartmentReport = apartmentReportFactory.create(KATOWICE, ReportType.NEWEST);
        assertEquals(1, apartmentReport.getCategories().size());
    }

    @Test
    void there_are_three_categories_for_daily_report() {
        ApartmentReport apartmentReport = apartmentReportFactory.create(KATOWICE, ReportType.DAILY);
        assertEquals(3, apartmentReport.getCategories().size());
    }

    /**
     * {@link CategoryFactoryTest#there_are_two_newest_apartments}
     */
    @Test
    void newest_report_contains_2_ads() {
        List<Ad> ads = apartmentReportFactory.create(KATOWICE, ReportType.NEWEST)
                .getCategories()
                .get(0)
                .getAds();
        assertEquals(2, ads.size());
    }

    @Test
    void non_empty_report_has_city_set() {
        String cityName = apartmentReportFactory.create(KATOWICE, ReportType.NEWEST)
                .getCity()
                .getName();
        assertEquals("Katowice", cityName);
    }

    @Test
    void non_empty_report_has_title() {
        String title = apartmentReportFactory.create(KATOWICE, ReportType.NEWEST)
                .getTitle();
        assertEquals("Nowe ogłoszenia! - Katowice", title);
    }
}
