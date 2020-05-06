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
    private static final String TITLE_PATTERN = "Mieszkania/domy do wynajęcia - %s";

    private final JavaMailSender emailSender;
    private final TemplateProcessor templateProcessor;

    @Override
    public void initialEmail(ApartmentReport apartmentReport, String email) {
        String city = apartmentReport.getCity().getName();
        String subject = String.format(TITLE_PATTERN, city);
        String html = templateProcessor.initialEmail(apartmentReport, email);
        try {
            MimeMessageWrapper message = prepareMessage(subject, html);
            sendInitialMessage(message, email);
        } catch (MessagingException e) {
            log.error("Unable to send initial email for {} subscription to {}", city, email, e);
        }
    }

    @Override
    public void notifySubscribers(ApartmentReport apartmentReport) {
        String city = apartmentReport.getCity().getName();
        String subject = String.format(TITLE_PATTERN, city);
        String html = templateProcessor.subscriptionEmail(apartmentReport);
        Set<String> subscribers = apartmentReport.getCity().getSubscribers();

        if (isEmpty(subscribers)) {
            log.warn("No one is subscribed to {} report!", apartmentReport.getCity());
            return;
        }

        try {
            MimeMessageWrapper message = prepareMessage(subject, html);
            log.info("Sending {} report to: {}}", apartmentReport.getCity().getName(), subscribers);
            notifySubscribers(message, subscribers);
        } catch (MessagingException e) {
            log.error("Unable to send subscription emails for {} to {}", city, subscribers, e);
        }
    }

    private MimeMessageWrapper prepareMessage(String subject, String content) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        message.setContent(content, "text/html; charset=utf-8");

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setSubject(subject);

        return new MimeMessageWrapper(message, helper);
    }

    private void sendInitialMessage(MimeMessageWrapper messageWrapper, String email) throws MessagingException {
        messageWrapper.helper.setTo(email);
        emailSender.send(messageWrapper.message);
    }

    private void notifySubscribers(MimeMessageWrapper messageWrapper, Set<String> emails) throws MessagingException {
        messageWrapper.helper.setBcc(emails.toArray(new String[0]));
        emailSender.send(messageWrapper.message);
    }

    private record MimeMessageWrapper(MimeMessage message, MimeMessageHelper helper){}
}
