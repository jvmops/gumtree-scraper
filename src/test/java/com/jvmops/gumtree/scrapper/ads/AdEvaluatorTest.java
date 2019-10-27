package com.jvmops.gumtree.scrapper.ads;

import com.jvmops.gumtree.scrapper.Main;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@ContextConfiguration(initializers = {AdEvaluatorTest.Initializer.class})
public class AdEvaluatorTest {

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private AdEvaluator adEvaluator;

    @ClassRule
    public static final GenericContainer TEST_MONGO = new GenericContainer("mongo:3.4.23-xenial")
            .withExposedPorts(27017);

    @Before
    public void setupDb() {
        LocalDate creationTime = LocalDate.parse("2019-10-27");
        Ad ad = Ad.builder()
                .title("Test ad")
                .creationDate(creationTime)
                .updates(List.of(creationTime))
                .build();
        adRepository.save(ad);
    }

    @Test
    public void creation_date_of_refreshed_ad_will_be_updated() {
        Ad scrappedAd = Ad.builder()
                .title("Test ad")
                .creationDate(LocalDate.parse("2019-10-28"))
                .build();

        adEvaluator.processAds(Stream.of(scrappedAd));

        Ad fromDb = adRepository.findByTitle("Test ad");
        Assert.assertEquals(fromDb.getCreationDate(), LocalDate.parse("2019-10-28"));
        Assert.assertEquals("Two creation dates should be present for refreshed ad", 2, fromDb.getUpdates().size());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.port=" + TEST_MONGO.getFirstMappedPort()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
