package com.jvmops.gumtree.report;

import java.util.Set;

public interface NotificationSender {
    void send(ApartmentReport apartmentReport);
    void send(ApartmentReport apartmentReport, Set<String> emails);
}