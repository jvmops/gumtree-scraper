package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.scrapper.Ad;
import com.jvmops.gumtree.scrapper.AdRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.jvmops.gumtree.notifications.ApartmentReportFactoryTest.Initializer;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(initializers = {Initializer.class})
@Testcontainers
class ApartmentReportFactoryTest {
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.port=" + MONGO.getFirstMappedPort()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Container
    private static final GenericContainer MONGO = new GenericContainer("mongo:3.4.23-xenial")
            .withExposedPorts(27017);

    @Autowired
    private ApartmentReportFactory apartmentReportFactory;
    @Autowired
    private AdRepository adRepository;

    @BeforeEach
    public void setupDb() {
        if (adRepository.count() == 0) {
            createTestAds().forEach(adRepository::save);
        }
    }

    @Test
    public void content_type_of_test_report_will_be_text_plain() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals("text/plain", apartmentReport.getContentType());
    }

    @Test
    public void there_will_be_two_newest_ads() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(2, apartmentReport.getNewApartments().size());
    }

    @Test
    public void there_will_be_one_gas_ad() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(1, apartmentReport.getGasApartments().size());
    }

    @Test
    public void there_will_be_two_cheapest_ads() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(2, apartmentReport.getCheapestApartments().size());
    }

    @Test
    public void test_report_will_have_15_lines() {
        var apartmentReport = apartmentReportFactory.create();
        assertEquals(15L, apartmentReport.getReport().lines().count());
    }

    private Stream<Ad> createTestAds() {
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDate dateNow = LocalDate.now();

        Ad newAd = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Just added ad")
                .price(2400)
                .creationTime(timeNow.minusHours(2))
                .gumtreeCreationDate(dateNow)
                .updates(List.of(dateNow))
                .build();

        Ad gasApartment = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Apartment with kuchnia gazowa (gas) in the kitchen")
                .price(2200)
                .creationTime(timeNow.minusHours(23))
                .gumtreeCreationDate(dateNow)
                .updates(List.of(dateNow))
                .build();

        Ad oldAd = Ad.builder()
                .title(UUID.randomUUID().toString())
                .description("Some two week old ad")
                .price(700)
                .creationTime(timeNow.minusWeeks(2))
                .gumtreeCreationDate(dateNow.minusWeeks(2))
                .updates(List.of(dateNow.minusWeeks(2)))
                .build();

        return Stream.of(newAd, gasApartment, oldAd);
    }
}