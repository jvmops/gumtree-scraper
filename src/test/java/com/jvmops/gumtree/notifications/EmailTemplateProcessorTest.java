package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.subscriptions.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Set;

@Disabled
@SpringBootTest(classes = Main.class)
class EmailTemplateProcessorTest extends JsonDataInitializer {
    private static final String SUBSCRIBERS_EMAIL = "subscriber@gumtree.jvmops.com";
    private static final City KATOWICE = City.builder()
            .name("Katowice")
            .subscribers(Set.of(SUBSCRIBERS_EMAIL))
            .build();

    @Autowired
    private EmailTemplateProcessor emailTemplateProcessor;
    @Autowired
    private ApartmentReportFactory apartmentReportFactory;

    @BeforeAll
    static void setup(@Autowired MongoTemplate mongoTemplate) {
        reloadAds(mongoTemplate, DUMPED_ADS);
    }

    @Test
    void initial_email_is_generated() {
        Assertions.assertDoesNotThrow(() ->
                emailTemplateProcessor.initialEmail(getReport(), SUBSCRIBERS_EMAIL)
        );
    }

    private ApartmentReport getReport() {
        return apartmentReportFactory.create(KATOWICE, ReportType.INITIAL);
    }
}
