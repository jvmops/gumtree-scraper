package com.jvmops.gumtree.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ApartmentReportType {
    INITIAL("Mieszkania/domy do wynajęcia - %s"),
    DAILY("Mieszkania/domy do wynajęcia - %s"),
    NEWEST("Mieszkania dodane w ciągu ostatnich 2 godzin! - %s");

    public final String titleTemplate;
}
