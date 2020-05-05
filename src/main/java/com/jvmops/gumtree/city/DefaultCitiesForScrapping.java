package com.jvmops.gumtree.city;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Profile("web")
@Component
@Slf4j
@AllArgsConstructor
class DefaultCitiesForScrapping {
    private static final City KATOWICE = City.builder()
            .id(ObjectId.get())
            .name("Katowice")
            .urlCode("v1c9008l3200285")
            .build();
    private static final City GLIWICE = City.builder()
            .id(ObjectId.get())
            .name("Gliwice")
            .urlCode("v1c9008l3200282")
            .build();
    private static final City KRAKOW = City.builder()
            .id(ObjectId.get())
            .name("Krakow")
            .urlCode("v1c9008l3200208")
            .build();

    private CityRepository cityRepository;

    @PostConstruct
    void addIfNoneSet() {
        if (cityRepository.count() == 0) {
            cityRepository.save(KATOWICE);
            cityRepository.save(GLIWICE);
            cityRepository.save(KRAKOW);
        }
    }
}
