package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.ApartmentReport;
import com.jvmops.gumtree.notifications.model.ApartmentReportType;
import com.jvmops.gumtree.notifications.ports.EmailSender;
import com.jvmops.gumtree.subscriptions.City;
import com.jvmops.gumtree.subscriptions.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    private static final String SUBSCRIBERS_EMAIL = "subscriber@gumtree.jvmops.com";
    private static final City KATOWICE = City.builder()
            .name("Katowice")
            .subscribers(Set.of(SUBSCRIBERS_EMAIL))
            .build();
    private static final City WROCLAW = City.builder()
            .name("Wroclaw")
            .build();

    private NotificationService notificationService;

    @Mock
    private ApartmentReportFactory apartmentReportFactory;
    @Mock
    private CityService cityService;
    @Mock
    private EmailSender emailSender;

    @BeforeEach
    void setup() {
        notificationService = new NotificationService(cityService, apartmentReportFactory, emailSender);
    }

    // initial email

    /**
     * If this was a project with real business value it would be terrible!
     */
    @Test
    void initial_email_is_not_generated_from_an_empty_report() {
        var emptyReport = ApartmentReport.empty(KATOWICE, ApartmentReportType.INITIAL);
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ApartmentReportType.INITIAL))
                .thenReturn(emptyReport);

        notificationService.initialEmail(KATOWICE, SUBSCRIBERS_EMAIL);

        Mockito.verify(emailSender, never())
                .initialEmail(any(), anyString());
    }

    @Test
    void initial_email_will_be_sent_to_the_new_subscriber() {
        var niceReport = ApartmentReport.builder()
                .apartmentReportType(ApartmentReportType.INITIAL)
                .build();
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ApartmentReportType.INITIAL))
                .thenReturn(niceReport);

        notificationService.initialEmail(KATOWICE, SUBSCRIBERS_EMAIL);

        Mockito.verify(emailSender, Mockito.times(1))
                .initialEmail(niceReport, SUBSCRIBERS_EMAIL);
    }

    // subscriptions

    @Test
    void city_without_subscribers_is_ignored() {
        stubCityService(WROCLAW);

        notificationService.notifySubscribers(ApartmentReportType.DAILY);

        Mockito.verify(emailSender, never())
                .notifySubscribers(any());
    }

    @Test
    void subscribers_wont_be_notified_about_an_empty_report() {
        stubCityService(KATOWICE);
        var emptyReport = ApartmentReport.empty(KATOWICE, ApartmentReportType.DAILY);
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ApartmentReportType.DAILY))
                .thenReturn(emptyReport);

        notificationService.notifySubscribers(ApartmentReportType.DAILY);

        Mockito.verify(emailSender, never())
                .notifySubscribers(any());
    }

    @Test
    void subscribers_will_be_notified_about_new_report() {
        stubCityService(KATOWICE);
        var apartmentReport = ApartmentReport.builder()
                .city(KATOWICE)
                .apartmentReportType(ApartmentReportType.DAILY)
                .build();
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ApartmentReportType.DAILY))
                .thenReturn(apartmentReport);

        notificationService.notifySubscribers(ApartmentReportType.DAILY);

        Mockito.verify(emailSender, times(1))
                .notifySubscribers(apartmentReport);

    }

    private void stubCityService(City katowice) {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(katowice));
    }
}