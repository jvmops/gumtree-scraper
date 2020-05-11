package com.jvmops.gumtree.notifications.model;

public record EmailWithReport(ApartmentReport report, String html) {}