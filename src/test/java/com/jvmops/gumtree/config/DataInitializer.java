package com.jvmops.gumtree.config;

import com.jvmops.gumtree.MongoTest;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Slf4j
abstract class DataInitializer extends MongoTest {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    protected Time time;

    @BeforeAll
    public static void clearData() {
        deleteAll();
    }

    @BeforeEach
    private void insertDataIfNecessary() {
        if (cityRepository.count() == 0) {
            cityRepository.saveAll(mapEmailsToCities());
        }
    }

    private List<City> mapEmailsToCities() {
        City katowice = City.builder()
                .id(ObjectId.get())
                .name("katowice")
                .emails(Set.of("jvmops+test1@gmail.com", "jvmops+test2e@gmail.com"))
                .creationTime(time.now())
                .build();
        City wroclaw = City.builder()
                .id(ObjectId.get())
                .name("wroclaw")
                .emails(Set.of())
                .creationTime(time.now())
                .build();
        return List.of(katowice, wroclaw);
    }
}
