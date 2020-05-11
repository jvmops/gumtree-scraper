package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import com.jvmops.gumtree.notifications.EmailTemplateProcessor.EmailWithReport;
import com.jvmops.gumtree.subscriptions.City;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.Set;

import static com.jvmops.gumtree.notifications.CategoryType.NEWS;
import static com.jvmops.gumtree.notifications.ReportType.*;

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
                emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL)
        );
    }

    @Test
    void email_from_newest_report_is_generated() {
        Assertions.assertDoesNotThrow(() ->
                emailTemplateProcessor.subscriptionEmail(generateReport(NEWEST))
        );
    }

    @Test
    void email_from_daily_report_is_generated() {
        Assertions.assertDoesNotThrow(() ->
                emailTemplateProcessor.subscriptionEmail(generateReport(DAILY))
        );
    }

    // INITIAL

    @Test
    void initial_email_contains_all_four_categories() {
        EmailWithReport email = emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL);
        Elements elements = categories(email);
        Assertions.assertEquals(4, elements.size());
    }

    @Test
    void category_consist_of_header() {
        EmailWithReport email = emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL);
        String headerContent = categoryHeader(email, NEWS);
        Assertions.assertEquals("Jeszcze ciepłe:", headerContent);
    }

    @Test
    void each_category_of_initial_email_contain_ads() {
        EmailWithReport email = emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL);
        categories(email).stream()
                .map(this::ads)
                .forEach(ads -> Assertions.assertFalse(ads.isEmpty()));
    }

    @Test
    void ad_price_is_rendered_properly() {
        EmailWithReport email = emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL);
        String adPrice = categories(email).stream()
                .map(this::ads)
                .map(this::price)
                .flatMap(Collection::stream)
                .findFirst()
                .map(Element::text)
                .orElseThrow(() -> new IllegalStateException(
                        String.format("There are ads prepared for this test in %s file! Html attribute names could change too.", DUMPED_ADS)));

        Assert.assertNotNull(adPrice);
        Assert.assertEquals("1900", adPrice);
    }

    @Test
    void initial_email_has_subscribe_button() {
        EmailWithReport email = emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL);
        Element subscribeButton = subscribeButton(email);
        Assert.assertNotNull(subscribeButton);
        Assert.assertEquals("Subskrybuj", subscribeButton.text());
    }

    @Test
    void subscribe_button_url_has_stuff_url_encoded() {
        EmailWithReport email = emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL);
        String url = subscribeButton(email)
                .selectFirst("a")
                .attr("href");
        Assert.assertEquals("http://localhost:8080/subscribe?email=subscriber%40gumtree.jvmops.com&city=Katowice", url);
    }

    @Test
    void initial_email_does_not_have_footer() {
        EmailWithReport email = emailTemplateProcessor.initialEmail(generateReport(INITIAL), SUBSCRIBERS_EMAIL);
        Element footer = footer(email);
        Assert.assertNull(footer);
    }

    // NEWEST / DAILY

    @Test
    void newest_report_has_footer() {
        EmailWithReport email = emailTemplateProcessor.subscriptionEmail(generateReport(NEWEST));
        Element footer = footer(email);
        Assert.assertNotNull(footer);
    }

    @Test
    void daily_report_has_footer() {
        EmailWithReport email = emailTemplateProcessor.subscriptionEmail(generateReport(DAILY));
        Element footer = footer(email);
        Assert.assertNotNull(footer);
    }

    @Test
    void footer_has_unsubscribe_url() {
        EmailWithReport email = emailTemplateProcessor.subscriptionEmail(generateReport(DAILY));
        String url = footer(email)
                .selectFirst("a")
                .attr("href");
        Assert.assertEquals("http://localhost:8080/unsubscribe?city=Katowice", url);
    }

    private ApartmentReport generateReport(ReportType reportType) {
        return switch (reportType) {
            case INITIAL -> apartmentReportFactory.create(KATOWICE, INITIAL);
            case NEWEST -> apartmentReportFactory.create(KATOWICE, NEWEST);
            case DAILY -> apartmentReportFactory.create(KATOWICE, DAILY);
        };
    }

    private Elements categories(EmailWithReport email) {
        return document(email)
                .select("div[name=categoryWrapper]");
    }

    private String categoryHeader(EmailWithReport email, CategoryType categoryType) {
        return document(email)
                .getElementById(categoryType.toString())
                .selectFirst("h4")
                .text();
    }

    private Elements ads(Element category) {
        return category.select("tr[name=ad]");
    }

    private Elements price(Elements ad) {
        return ad.select("td[name=price]");
    }

    private Element subscribeButton(EmailWithReport email) {
        return document(email)
                .getElementById("cto_button");
    }

    private Element footer(EmailWithReport email) {
        return document(email)
                .getElementById("footer");
    }

    private Document document(EmailWithReport email) {
        return Jsoup.parse(email.html());
    }
}
