package com.jvmops.gumtree.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum ReportType {
    INITIAL("Mieszkania/domy do wynajęcia - %s"),
    DAILY("Mieszkania/domy do wynajęcia - %s"),
    NEWEST("Nowe ogłoszenia! - %s");

    private String titleTemplate;
}
