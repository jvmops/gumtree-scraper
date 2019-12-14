package com.jvmops.gumtree.config;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.MongoTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(
        initializers = MongoTest.Initializer.class)
public class ManagedConfigurationTest extends DataInitializer {
    @Autowired
    private ManagedConfiguration managedConfiguration;

    @Test
    void config_will_provide_two_cities_to_scrap() {
        var cities = managedConfiguration.getCities();
        assertEquals(2, cities.size());
    }

    @Test
    void there_are_two_emails_configured_for_katowice() {
        Set<String> emails = managedConfiguration.getEmails("katowice");
        assertEquals(2, emails.size());
    }

    @Test
    void wroclaw_is_only_scrapped_that_means_notifications_will_not_be_send() {
        Set<String> emails = managedConfiguration.getEmails("wroclaw");
        assertEquals(0, emails.size());
    }

    @Test
    void unknown_city_is_handled() {
        Set<String> emails = managedConfiguration.getEmails("no_such_city");
        assertEquals(0, emails.size());
    }
}
