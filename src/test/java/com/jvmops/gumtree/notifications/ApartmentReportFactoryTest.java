package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.AdCollectionTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class ApartmentReportFactoryTest extends AdCollectionTest {

    @Autowired
    private ApartmentReportFactory apartmentReportFactory;

    @Test
    void content_type_of_test_report_will_be_text_plain() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals("text/plain", apartmentReport.getContentType());
    }

    @Test
    void there_will_be_two_newest_ads() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(2, apartmentReport.getNewApartments().size());
    }

    @Test
    void there_will_be_one_gas_ad() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(1, apartmentReport.getGasApartments().size());
    }

    @Test
    void there_will_be_two_cheapest_ads() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(2, apartmentReport.getCheapestApartments().size());
    }

    @Test
    void test_report_will_have_15_lines() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(15L, apartmentReport.getReport().lines().count());
    }
}