package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.subscriptions.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
class ApartmentReportFactoryTest extends DataInitializer {

    private static final City KATOWICE = new City("Katowice");
    private static final City WROCLAW = new City("Wroclaw");

    @Autowired
    private ApartmentReportFactory apartmentReportFactory;

    @Test
    void there_is_one_persisted_ad_for_wroclaw() {
        var apartmentReport = apartmentReportFactory.create(WROCLAW);
        assertEquals(1, apartmentReport.getNewApartments().size());
        assertEquals(1, apartmentReport.getDishwasherAndGasApartments().size());
        assertEquals(0, apartmentReport.getGasOnlyApartments().size());
        assertEquals(0, apartmentReport.getDishwasherOnlyApartments().size());
        assertEquals(1, apartmentReport.getCheapestApartments().size());
    }

    @Test
    void there_are_three_newest_ads_for_katowice() {
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(3, apartmentReport.getNewApartments().size());
    }

    @Test
    void there_is_one_ad_with_both_dishwasher_and_gas() {
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(1, apartmentReport.getDishwasherAndGasApartments().size());
    }

    @Test
    void there_is_one_distinct_apartment_with_gas() {
        // if gas apartment contains dishwasher too it does not count here
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(1, apartmentReport.getGasOnlyApartments().size());
    }

    @Test
    void there_is_one_distinct_apartment_with_dishwasher() {
        // if dishwasher apartment contains gas too it does not count here
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(1, apartmentReport.getDishwasherOnlyApartments().size());
    }

    @Test
    void all_ads_from_the_last_5_days_from_katowice_counts_as_cheapest() {
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(3, apartmentReport.getCheapestApartments().size());
    }
}