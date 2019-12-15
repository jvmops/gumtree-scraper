package com.jvmops.gumtree.city;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
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
    public void email_can_be_unsubscribed_from_notifications() {
        cityService.stopNotifications("to.be.removed@gmail.com");
        City wroclaw = cityService.findCityByName("wroclaw");
        assertFalse(wroclaw.getEmails().contains("to.be.removed@gmail.com"));
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

    @Test
    void wroclaw_is_only_scrapped_that_means_notifications_will_not_be_send() {
        Set<String> emails = cityService.emails("wroclaw");
        assertEquals(0, emails.size());
    }
}
