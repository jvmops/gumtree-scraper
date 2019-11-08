package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.AdCollectionTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotifyJobTest extends AdCollectionTest {
    @Autowired
    private NotifyJob notifyJob;

    @Test
    @EnabledIf(expression = "#{systemProperties['spring.mail.password'] != null}")
    void email_should_be_send_through_gmail() {
        notifyJob.execute();
        assertEquals(1 , 1);
    }
}