package com.jvmops.gumtree.city;

import com.jvmops.gumtree.Time;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Slf4j
abstract class DataInitializer {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    protected Time time;

    @BeforeEach
    private void insertDataIfNecessary() {
        cityRepository.deleteAll();
        cityRepository.saveAll(mapEmailsToCities());
    }

    private List<City> mapEmailsToCities() {
        City katowice = City.builder()
                .id(ObjectId.get())
                .name("katowice")
                .notifications(Set.of("jvmops@gmail.com", "jvmops+test@gmail.com"))
                .creationTime(time.now())
                .build();
        City wroclaw = City.builder()
                .id(ObjectId.get())
                .name("wroclaw")
                .notifications(Set.of("jvmops@gmail.com"))
                .creationTime(time.now())
                .build();
        return List.of(katowice, wroclaw);
    }
}
