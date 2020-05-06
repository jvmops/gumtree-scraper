package com.jvmops.gumtree.subscriptions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("web")
@Component
@Slf4j
@AllArgsConstructor
class DefaultCitiesForScrapping {
    private CityService cityService;

    @PostConstruct
    void addIfNoneSet() {
        if (cityService.cities().size() == 0) {
            cityService.add("Katowice", "v1c9008l3200285");
            cityService.add("Gliwice", "v1c9008l3200282");
            cityService.add("Krakow", "v1c9008l3200208");
        }
    }
}
