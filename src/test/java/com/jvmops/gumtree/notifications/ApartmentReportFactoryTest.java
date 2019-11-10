package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
class ApartmentReportFactoryTest extends ReportDataInitializer {

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