package com.jvmops.gumtree.report;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import com.jvmops.gumtree.city.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
class ApartmentReportFactoryTest extends DataInitializer {

    @Autowired
    private ApartmentReportFactory apartmentReportFactory;

    @Test
    void there_are_no_persisted_ads_from_wroclaw() {
        var apartmentReport = apartmentReportFactory.create(new City("wroclaw"));
        assertEquals(0, apartmentReport.getNewApartments().size());
        assertEquals(0, apartmentReport.getGasApartments().size());
        assertEquals(0, apartmentReport.getCheapestApartments().size());
    }

    @Test
    void there_will_be_two_newest_ads() {
        var apartmentReport = apartmentReportFactory.create(new City("katowice"));
        assertEquals(2, apartmentReport.getNewApartments().size());
    }

    @Test
    void there_will_be_one_gas_ad() {
        var apartmentReport = apartmentReportFactory.create(new City("katowice"));
        assertEquals(1, apartmentReport.getGasApartments().size());
    }

    @Test
    void there_will_be_two_cheapest_ads() {
        var apartmentReport = apartmentReportFactory.create(new City("katowice"));
        assertEquals(2, apartmentReport.getCheapestApartments().size());
    }

    @Test
    void test_report_will_have_15_lines() {
        var apartmentReport = apartmentReportFactory.create(new City("katowice"));
        assertEquals(15L, apartmentReport.getReport().lines().count());
    }
}