package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
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
import java.util.List;
import java.util.stream.Stream;

import static com.jvmops.gumtree.scrapper.AdEvaluatorTest.Initializer;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = Main.class)
@ContextConfiguration(initializers = {Initializer.class})
@Testcontainers
class AdEvaluatorTest {
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.port=" + MONGO.getFirstMappedPort()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private AdEvaluator adEvaluator;

    @Container
    private static final GenericContainer MONGO = new GenericContainer("mongo:3.4.23-xenial")
            .withExposedPorts(27017);

    @BeforeEach
    public void setupDb() {
        adRepository.deleteAll();
        adRepository.save(testAd());
    }

    @Test
    public void creation_date_of_refreshed_ad_will_be_updated() {
        LocalDate nextDay = LocalDate.parse("2019-10-28");
        Ad scrappedAd = Ad.builder()
                .title("Test ad")
                .gumtreeCreationDate(nextDay)
                .build();

        adEvaluator.processAds(Stream.of(scrappedAd));

        Ad fromDb = adRepository.findByTitle("Test ad");
        assertEquals(2, fromDb.getUpdates().size());
    }

    private Ad testAd() {
        LocalDate creationTime = LocalDate.parse("2019-10-27");
        return Ad.builder()
                .title("Test ad")
                .gumtreeCreationDate(creationTime)
                .updates(List.of(creationTime))
                .build();
    }
}
