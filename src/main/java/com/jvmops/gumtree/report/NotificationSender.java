package com.jvmops.gumtree.report;

public interface NotificationSender {
    boolean send(String content, String contentType);
}