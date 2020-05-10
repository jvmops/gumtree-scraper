package com.jvmops.gumtree.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    NEWS("Jeszcze ciepłe:"),
    DISHWASHER_AND_GAS("Mieszkania ze zmywarką i gazem:"),
    DISHWASHER_ONLY("Mieszkania ze zmywarką:"),
    CHEAPEST("Najtańsze oferty:");

    private String header;
}
