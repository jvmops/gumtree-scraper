package com.jvmops.gumtree.report;

interface NotificationSender {
    boolean send(String content, String contentType);
}