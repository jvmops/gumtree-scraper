package com.jvmops.gumtree.notifications;

public interface NotificationSender {
    boolean send(String content, String contentType);
}