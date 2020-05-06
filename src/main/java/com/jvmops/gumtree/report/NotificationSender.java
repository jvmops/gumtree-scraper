package com.jvmops.gumtree.report;

import java.util.Set;

public interface NotificationSender {
    void notifySubscribers(ApartmentReport apartmentReport);
    void initialEmail(ApartmentReport apartmentReport, String emails);
    void notifySubscribers(ApartmentReport apartmentReport, Set<String> emails);
}