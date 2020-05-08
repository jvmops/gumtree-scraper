package com.jvmops.gumtree.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.lang.String.format;

@AllArgsConstructor
@Getter
enum ReportType {
    INITIAL("Mieszkania/domy do wynajęcia - %s"),
    DAILY("Mieszkania/domy do wynajęcia - %s"),
    NEWEST(format("Ogłoszenia sprzed %s godzin", NewestApartments.PERIOD) + " - %s");

    private String titleTemplate;
}
