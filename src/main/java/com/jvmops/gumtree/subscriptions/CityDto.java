package com.jvmops.gumtree.subscriptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CityDto {
    private String name;
    private Set<String> emails;
    private LocalDateTime modificationTime;
}
