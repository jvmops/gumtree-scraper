package com.jvmops.gumtree.notifications.ports;

import com.jvmops.gumtree.notifications.model.ApartmentReport;

import javax.validation.constraints.Email;

public interface EmailSender {
    void initialEmail(ApartmentReport apartmentReport, @Email String subscriberWannabe);
    void notifySubscribers(ApartmentReport apartmentReport);
}
