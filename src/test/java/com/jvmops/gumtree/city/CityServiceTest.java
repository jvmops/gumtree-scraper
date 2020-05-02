package com.jvmops.gumtree.city;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.Time;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

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
    private Time time;

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
                () -> cityService.addCity("Wroclaw")
        );
    }

    @Test
    void quering_an_unknown_city_will_result_in_exception() {
        assertThrows(
                CityNotFound.class,
                () -> cityService.emails("no_such_city")
        );
    }

    @Test
    public void you_can_subscribe_to_notifications_with_email() {
        cityService.subscribeToNotifications("Wroclaw", "other@gmail.com");
        City wroclaw = cityService.findCityByName("Wroclaw");
        assertEquals(2, wroclaw.getNotifications().size());
    }

    @Test
    public void email_can_be_unsubscribed_from_all_notification_lists() {
        cityService.stopNotifications("jvmops@gmail.com");
        City katowice = cityService.findCityByName("Katowice");
        City wroclaw = cityService.findCityByName("Wroclaw");
        assertEquals(1, katowice.getNotifications().size());
        assertEquals(0, wroclaw.getNotifications().size());
    }

    @Test
    public void new_city_with_empty_mailing_list_can_be_added() {
        cityService.subscribeToNotifications("gliwice", null);
        City wroclaw = cityService.findCityByName("gliwice");
        assertEquals(0, wroclaw.getNotifications().size());
    }

    private List<City> createCities() {
        City katowice = City.builder()
                .id(ObjectId.get())
                .name("Katowice")
                .notifications(Set.of("jvmops@gmail.com", "jvmops+test@gmail.com"))
                .creationTime(time.now())
                .build();
        City wroclaw = City.builder()
                .id(ObjectId.get())
                .name("Wroclaw")
                .notifications(Set.of("jvmops@gmail.com"))
                .creationTime(time.now())
                .build();
        return List.of(katowice, wroclaw);
    }
}
