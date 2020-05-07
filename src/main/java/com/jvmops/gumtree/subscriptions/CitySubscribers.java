package com.jvmops.gumtree.subscriptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CitySubscribers {
    private String cityName;
    private Set<String> emails;
    private LocalDateTime modificationTime;
}
