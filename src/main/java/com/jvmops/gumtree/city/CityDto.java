package com.jvmops.gumtree.city;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private String name;
    private Set<String> emails;
    private LocalDateTime modificationTime;
}
