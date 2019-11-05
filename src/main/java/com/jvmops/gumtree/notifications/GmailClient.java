package com.jvmops.gumtree.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Lazy
@Slf4j
public class GmailClient implements NotificationSender {
    private static final String TITLE = "Wroclaw apartments report";

    @Override
    public boolean send(String content, String contentType) {
        log.info("Email is about to be send..");
        return false;
    }

    @PostConstruct
    public void log() {
        log.warn("Lazy GmailClient created. WIP - does not send mails for now...");
    }
}
