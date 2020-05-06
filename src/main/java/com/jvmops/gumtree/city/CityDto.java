package com.jvmops.gumtree.city;

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
