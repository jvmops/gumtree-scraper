package com.jvmops.gumtree.report;

import java.util.Set;

interface NotificationSender {
    boolean send(ApartmentReport apartmentReport);
    boolean send(ApartmentReport apartmentReport, Set<String> emails);
}