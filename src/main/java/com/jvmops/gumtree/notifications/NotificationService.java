package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.ApartmentReport;
import com.jvmops.gumtree.notifications.model.ApartmentReportType;
import com.jvmops.gumtree.notifications.ports.EmailSender;
import com.jvmops.gumtree.subscriptions.model.City;
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
    private EmailSender emailSender;

    public void initialEmail(City city, String subscriberWannabe) {
        ApartmentReport apartmentReport = apartmentReportFactory.create(city, ApartmentReportType.INITIAL);
        if (apartmentReport.isEmpty()) {
            return;
        }
        emailSender.initialEmail(apartmentReport, subscriberWannabe);
    }

    public void notifySubscribers(ApartmentReportType apartmentReportType) {
        cityService.cities().stream()
                .map(city -> apartmentReportFactory.create(city, apartmentReportType))
                .filter(Predicate.not(ApartmentReport::isEmpty))
                .forEach(apartmentReport -> emailSender.notifySubscribers(apartmentReport));
    }
}
