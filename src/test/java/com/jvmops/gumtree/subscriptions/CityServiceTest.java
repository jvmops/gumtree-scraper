package com.jvmops.gumtree.subscriptions;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.subscriptions.model.City;
import com.jvmops.gumtree.subscriptions.model.CityNotFound;
import com.jvmops.gumtree.subscriptions.model.Subscription;
import com.jvmops.gumtree.subscriptions.ports.CityRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Main.class)
public class CityServiceTest {
    @Autowired
    private CityService cityService;

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private Clock clock;

    @BeforeEach
    private void saveCities() {
        cityRepository.saveAll(createCities());
    }

    @AfterEach
    private void deleteCities() {
        cityRepository.deleteAll();
    }

    @Test
    public void city_name_is_unique() {
        assertThrows(
                DuplicateKeyException.class,
                () -> cityService.add("Wroclaw", "235235")
        );
    }

    @Test
    void quering_an_unknown_city_will_result_in_exception() {
        assertThrows(
                CityNotFound.class,
                () -> cityService.getByName("no_such_city")
        );
    }

    @Test
    public void you_can_subscribe_to_notifications_with_email() {
        Subscription subscription = Subscription.builder()
                .city("Wroclaw")
                .email("other@gmail.com")
                .build();
        cityService.start(subscription);
        City wroclaw = cityService.getByName("Wroclaw");
        assertEquals(2, wroclaw.getSubscribers().size());
    }

    @Test
    public void new_city_with_empty_mailing_list_can_be_added() {
        cityService.add("gliwice", "523523");
        City gliwice = cityService.getByName("gliwice");
        assertEquals(0, gliwice.getSubscribers().size());
    }

    private List<City> createCities() {
        City katowice = City.builder()
                .id(ObjectId.get())
                .name("Katowice")
                .subscribers(Set.of("jvmops@gmail.com", "jvmops+test@gmail.com"))
                .creationTime(LocalDateTime.now(clock))
                .build();
        City wroclaw = City.builder()
                .id(ObjectId.get())
                .name("Wroclaw")
                .subscribers(Set.of("jvmops@gmail.com"))
                .creationTime(LocalDateTime.now(clock))
                .build();
        return List.of(katowice, wroclaw);
    }
}
