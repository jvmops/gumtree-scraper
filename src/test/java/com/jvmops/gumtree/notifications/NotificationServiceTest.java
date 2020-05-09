package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.EmailTemplateProcessor.EmailWithReport;
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
    private EmailTemplateProcessor emailTemplateProcessor;
    @Mock
    private NotificationSender notificationSender;

    @BeforeEach
    void setup() {
        notificationService = new NotificationService(cityService, apartmentReportFactory, emailTemplateProcessor, notificationSender);
    }

    // initial email

    /**
     * If this was a project with real business value it would be terrible!
     */
    @Test
    void initial_email_is_not_generated_from_an_empty_report() {
        ApartmentReport emptyReport = ApartmentReport.empty(KATOWICE, ReportType.INITIAL);
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ReportType.INITIAL))
                .thenReturn(emptyReport);

        notificationService.initialEmail(KATOWICE, SUBSCRIBERS_EMAIL);

        Mockito.verify(emailTemplateProcessor, never())
                .initialEmail(emptyReport, SUBSCRIBERS_EMAIL);
        Mockito.verify(notificationSender, never())
                .initialEmail(any(), anyString());
    }

    @Test
    void initial_email_will_be_sent_to_the_new_subscriber() {
        ApartmentReport emptyReport = ApartmentReport.empty(KATOWICE, ReportType.INITIAL);
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ReportType.INITIAL))
                .thenReturn(emptyReport);

        notificationService.initialEmail(KATOWICE, SUBSCRIBERS_EMAIL);

        Mockito.verify(emailTemplateProcessor, never())
                .initialEmail(emptyReport, SUBSCRIBERS_EMAIL);
    }

    // subscriptions

    @Test
    void city_without_subscribers_is_ignored() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(WROCLAW));

        notificationService.notifySubscribers(ReportType.DAILY);

        Mockito.verify(emailTemplateProcessor, never())
                .subscriptionEmail(any());
        Mockito.verify(notificationSender, never())
                .notifySubscribers(any());
    }

    @Test
    void subscribers_wont_be_notified_about_an_empty_report() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(KATOWICE));
        ApartmentReport emptyReport = ApartmentReport.empty(KATOWICE, ReportType.DAILY);
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ReportType.DAILY))
                .thenReturn(emptyReport);

        notificationService.notifySubscribers(ReportType.DAILY);

        Mockito.verify(emailTemplateProcessor, never())
                .subscriptionEmail(any());
    }

    @Test
    void subscribers_will_be_notified_about_new_report() {
        Mockito.when(cityService.cities())
                .thenReturn(Set.of(KATOWICE));
        // report
        ApartmentReport report = ApartmentReport.builder()
                .city(KATOWICE)
                .reportType(ReportType.DAILY)
                .build();
        Mockito.when(apartmentReportFactory
                .create(KATOWICE, ReportType.DAILY))
                .thenReturn(report);
        // email
        EmailWithReport email = new EmailWithReport(report, "email content");
        Mockito.when(emailTemplateProcessor.subscriptionEmail(report))
                .thenReturn(email);

        notificationService.notifySubscribers(ReportType.DAILY);

        Mockito.verify(notificationSender, times(1))
                .notifySubscribers(email);
    }
}