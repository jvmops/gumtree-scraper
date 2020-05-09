package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.EmailTemplateProcessor.EmailWithReport;

public interface NotificationSender {
    void initialEmail(EmailWithReport email, String subscriberWannabe);
    void notifySubscribers(EmailWithReport email);
}