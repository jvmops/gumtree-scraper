package com.jvmops.gumtree.notifications.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class Statistics {
    private Map<String, Integer> allPerDay;
    private Map<String, Long>  newPerDay;
}
