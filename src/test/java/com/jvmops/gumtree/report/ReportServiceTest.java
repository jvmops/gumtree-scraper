package com.jvmops.gumtree.report;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.city.City;
import com.jvmops.gumtree.city.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

//TODO: improve this test
@SpringBootTest(classes = Main.class)
class ReportServiceTest extends DataInitializer {
    public static final City KATOWICE = new City("Katowice");

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
    void subscribers_can_be_notified() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(KATOWICE));

        reportService.notifySubscribers();

        Mockito.verify(notificationSender).notifySubscribers(Mockito.any());
    }

    @Test
    void initial_email_can_be_send() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(new City("Katowice")));

        reportService.initialEmail(KATOWICE, "test@gmail.com");

        Mockito.verify(notificationSender).initialEmail(
                Mockito.any(),
                Mockito.eq("test@gmail.com")
        );
    }
}