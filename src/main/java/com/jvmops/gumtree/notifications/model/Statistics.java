package com.jvmops.gumtree.notifications.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Builder
@Getter
public class Statistics {
    private Map<LocalDate, Integer> allPerDay;
    private Map<LocalDate, Integer>  newPerDay;
}
