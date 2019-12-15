package com.jvmops.gumtree.report;

import java.util.Set;

interface NotificationSender {
    void send(ApartmentReport apartmentReport);
    void send(ApartmentReport apartmentReport, Set<String> emails);
}