package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.EmailTemplateProcessor.EmailWithReport;
import com.jvmops.gumtree.subscriptions.City;
import com.jvmops.gumtree.subscriptions.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
public class NotificationService {
    private CityService cityService;
    private ApartmentReportFactory apartmentReportFactory;
    private EmailTemplateProcessor emailTemplateProcessor;
    private NotificationSender notificationSender;

    public void initialEmail(City city, String subscriberWannabe) {
        ApartmentReport apartmentReport = apartmentReportFactory.create(city, ReportType.INITIAL);
        if (apartmentReport.isEmpty()) {
            return;
        }

        EmailWithReport email = emailTemplateProcessor.initialEmail(apartmentReport, subscriberWannabe);
        notificationSender.initialEmail(email, subscriberWannabe);
    }

    @SuppressWarnings("squid:S3864")
    public void notifySubscribers(ReportType reportType) {
        cityService.cities().stream()
                .filter(this::hasSubscribers)
                .peek(city -> log.info("Creating {} apartment report", city.getName()))
                .map(city -> apartmentReportFactory.create(city, reportType))
                .filter(Predicate.not(ApartmentReport::isEmpty))
                .map(emailTemplateProcessor::subscriptionEmail)
                .peek(email -> log.info("Preparing to notify {} subscribers about new {} report", email.report().getCity().getName(), reportType))
                .forEach(notificationSender::notifySubscribers);
    }

    private boolean hasSubscribers(City city) {
        boolean hasSubscribers = city.hasSubscribers();
        if (!hasSubscribers) {
            log.info("{} apartment report has no subscribers", city.getName());
        }
        return hasSubscribers;
    }
}
