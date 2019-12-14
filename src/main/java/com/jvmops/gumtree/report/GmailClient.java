package com.jvmops.gumtree.report;

import com.jvmops.gumtree.config.ManagedConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Set;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
class GmailClient implements NotificationSender {
    private static final String TITLE_PATTERN = "%s apartments report";

    private final ManagedConfiguration config;
    private final JavaMailSender emailSender;

    @Override
    public boolean send(ApartmentReport apartmentReport) {
        Set<String> emailAddresses = config.getEmails(apartmentReport.getCity());
        boolean sent = false;
        try {
            sendEmail(emailAddresses, apartmentReport);
            sent = true;
        } catch (MessagingException e) {
            log.error("Unable to send email to {}", emailAddresses, e);
        }
        return sent;
    }

    void sendEmail(Set<String> to, ApartmentReport apartmentReport) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setBcc(to.toArray(new String[0]));
        helper.setSubject(String.format(TITLE_PATTERN, apartmentReport.getCity()));
        helper.setText(apartmentReport.getReport());

        emailSender.send(message);
    }
}
