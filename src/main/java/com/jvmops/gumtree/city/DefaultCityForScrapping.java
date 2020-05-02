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
class DefaultCityForScrapping {
    private static final City KATOWICE = City.builder()
            .id(ObjectId.get())
            .name("Katowice")
            .notifications(Set.of("jvmops+default@gmail.com"))
            .build();

    private CityRepository cityRepository;

    @PostConstruct
    void addIfNoneSet() {
        if (cityRepository.count() == 0) {
            cityRepository.save(KATOWICE);
        }
    }
}
