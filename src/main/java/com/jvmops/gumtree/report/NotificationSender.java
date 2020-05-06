package com.jvmops.gumtree.report;

public interface NotificationSender {
    void initialEmail(ApartmentReport apartmentReport, String emails);
    void notifySubscribers(ApartmentReport apartmentReport);
}