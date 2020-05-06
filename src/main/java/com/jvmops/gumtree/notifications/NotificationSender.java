package com.jvmops.gumtree.notifications;

public interface NotificationSender {
    void initialEmail(ApartmentReport apartmentReport, String emails);
    void notifySubscribers(ApartmentReport apartmentReport);
}