package com.jvmops.gumtree.subscriptions.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder
public class CitySubscribers {
    String cityName;
    Set<String> emails;
}
