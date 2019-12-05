package com.jvmops.gumtree.report;

interface NotificationSender {
    boolean send(ApartmentReport apartmentReport);
}