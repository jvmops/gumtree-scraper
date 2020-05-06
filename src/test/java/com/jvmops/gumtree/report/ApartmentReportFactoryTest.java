package com.jvmops.gumtree.report;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.city.City;
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
    void there_is_one_persisted_ad_for_wroclaw_that_meets_all_criterias() {
        var apartmentReport = apartmentReportFactory.create(WROCLAW);
        assertEquals(1, apartmentReport.getNewApartments().size());
        assertEquals(1, apartmentReport.getGasApartments().size());
        assertEquals(1, apartmentReport.getDishwasherApartments().size());
        assertEquals(1, apartmentReport.getDishwasherAndGasApartments().size());
        assertEquals(1, apartmentReport.getCheapestApartments().size());
    }

    @Test
    void there_are_three_newest_ads_for_katowice() {
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(3, apartmentReport.getNewApartments().size());
    }

    @Test
    void there_are_two_gas_ads_for_katowice() {
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(2, apartmentReport.getGasApartments().size());
    }

    @Test
    void there_are_two_ads_from_katowice_with_dishwasher() {
        // search is case insensitive and polish letters are handled
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(2, apartmentReport.getDishwasherApartments().size());
    }

    @Test
    void there_is_one_ad_with_both_dishwasher_and_gas() {
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(1, apartmentReport.getDishwasherAndGasApartments().size());
    }

    @Test
    void all_ads_from_the_last_5_days_from_katowice_counts_as_cheapest() {
        var apartmentReport = apartmentReportFactory.create(KATOWICE);
        assertEquals(3, apartmentReport.getCheapestApartments().size());
    }
}