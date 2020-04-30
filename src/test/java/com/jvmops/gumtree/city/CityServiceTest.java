package com.jvmops.gumtree.city;

import com.jvmops.gumtree.Main;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
public class CityServiceTest extends DataInitializer {
    @Autowired
    private CityService cityService;

    @Test
    public void city_name_is_unique() {
        assertThrows(
                DuplicateKeyException.class,
                () -> cityService.addCity("wroclaw")
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
    public void email_can_be_unsubscribed_from_notifications_globally() {
        cityService.stopNotifications("jvmops@gmail.com");
        City katowice = cityService.findCityByName("katowice");
        City wroclaw = cityService.findCityByName("wroclaw");
        assertEquals(1, katowice.getNotifications().size());
        assertEquals(0, wroclaw.getNotifications().size());
    }

    @Test
    public void email_can_subscribed_to_notifications() {
        cityService.subscribeToNotifications("wroclaw", "other@gmail.com");
        City wroclaw = cityService.findCityByName("wroclaw");
        assertEquals(2, wroclaw.getNotifications().size());
    }

    @Test
    public void new_city_with_empty_mailing_list_can_be_added() {
        cityService.subscribeToNotifications("gliwice", null);
        City wroclaw = cityService.findCityByName("gliwice");
        assertEquals(0, wroclaw.getNotifications().size());
    }

    @Test
    void config_will_provide_two_cities_to_scrap() {
        var cities = cityService.cities();
        assertEquals(2, cities.size());
    }

    @Test
    void there_are_two_emails_configured_for_katowice() {
        Set<String> emails = cityService.emails("katowice");
        assertEquals(2, emails.size());
    }
}
