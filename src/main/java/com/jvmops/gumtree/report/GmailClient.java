package com.jvmops.gumtree.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Lazy
@Component
@Slf4j
@RequiredArgsConstructor
class GmailClient implements NotificationSender {
    private static final String TITLE_PATTERN = "%s apartments report";

    private final JavaMailSender emailSender;

    @Override
    public void send(ApartmentReport apartmentReport) {
        Set<String> emailAddresses = apartmentReport.getCity().getNotifications();
        send(apartmentReport, emailAddresses);
    }

    @Override
    public void send(ApartmentReport apartmentReport, Set<String> emails) {
        if (isEmpty(emails)) {
            return;
        }

        try {
            sendEmail(apartmentReport, emails);
        } catch (MessagingException e) {
            log.error("Unable to send email to {}", emails, e);
        }
    }

    void sendEmail(ApartmentReport apartmentReport, Set<String> emails) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setBcc(emails.toArray(new String[0]));
        helper.setSubject(String.format(TITLE_PATTERN, apartmentReport.getCity()));
        helper.setText(apartmentReport.getReport());

        emailSender.send(message);
    }
}
