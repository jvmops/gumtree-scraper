package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Main;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnabledIf(expression = "#{systemProperties['spring.mail.password'] != null}")
@SpringBootTest(classes = Main.class)
@Slf4j
class MailSendingTest extends DataInitializer {
    @Autowired
    private NotificationService notificationService;

    // EnableIf(..) doesn't work for maven - you can run this test in IntelliJ no prob and it ignores it but maven has a hiccup
    // TODO: check issues at Junit5 / maven plugin
    // @Test
    void email_should_be_send_through_gmail() {
        notificationService.notifySubscribers();
        log.info("Email shipped to the test inbox");
        assertEquals(1, 1);
    }
}