package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.config.GumtreeScrapperProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
public class GmailClient implements NotificationSender {
    private static final String TITLE_PATTERN = "%s :: Wroclaw apartments report";

    final private GumtreeScrapperProperties gumtreeScrapperProperties;
    final private JavaMailSender emailSender;

    @Override
    public boolean send(String content, String contentType) {
        log.info("Email is about to be send..");
        try {
            sendEmail(gumtreeScrapperProperties.getEmailAddresses(), content);
        } catch (MessagingException e) {
            log.error("Unable to send email", e);
        }
        return false;
    }

    void sendEmail(List<String> to, String text) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        helper.setTo(to.toArray(new String[0]));
        helper.setSubject(String.format(TITLE_PATTERN, LocalDate.now()));
        helper.setText(text);

        emailSender.send(message);
    }
}
