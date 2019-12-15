package com.jvmops.gumtree.report;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import com.jvmops.gumtree.city.City;
import com.jvmops.gumtree.city.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
class ReportServiceTest extends DataInitializer {
    @Autowired
    private ApartmentReportFactory apartmentReportFactory;
    @Mock
    private CityService cityService;
    @Mock
    private NotificationSender notificationSender;

    private ReportService reportService;

    @BeforeEach
    void setup() {
        reportService = new ReportService(apartmentReportFactory, notificationSender, cityService);
    }

    @Test
    void katowice_apartment_report_will_be_send_to_the_concerned_parties() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(new City("katowice")));

        reportService.createReportAndNotifyForEachCity();

        Mockito.verify(notificationSender).send(Mockito.any());
    }

    @Test
    void report_can_be_send_on_demand() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(new City("katowice")));

        reportService.createReportAndNotifySingleEmail("katowice", "test@gmail.com");

        Mockito.verify(notificationSender).send(
                Mockito.any(),
                Mockito.eq(Set.of("test@gmail.com"))
        );
    }
}