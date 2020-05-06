package com.jvmops.gumtree.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Lazy
@Component
@Slf4j
@RequiredArgsConstructor
class GmailClient implements NotificationSender {
    private static final String TITLE_PATTERN = "Mieszkania/domy do wynajęcia - %s";

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void initialEmail(ApartmentReport apartmentReport, String email) {
        try {
            MimeMessageWrapper message = prepareMessage(apartmentReport, EmailType.INITIAL);
            initialEmail(message, email);
        } catch (MessagingException e) {
            log.error("Unable to send email to {}", email, e);
        }
    }

    @Override
    public void notifySubscribers(ApartmentReport apartmentReport) {
        Set<String> emails = apartmentReport.getCity().getNotifications();
        notifySubscribers(apartmentReport, emails);
    }

    @Override
    public void notifySubscribers(ApartmentReport apartmentReport, Set<String> emails) {
        if (isEmpty(emails)) {
            log.warn("No one is subscribed to {} report!", apartmentReport.getCity());
            return;
        }

        try {
            MimeMessageWrapper message = prepareMessage(apartmentReport, EmailType.SUBSCRIPTION);
            notifySubscribers(message, emails);
        } catch (MessagingException e) {
            log.error("Unable to send email to {}", emails, e);
        }
    }

    private MimeMessageWrapper prepareMessage(ApartmentReport apartmentReport, EmailType emailType) throws MessagingException {
        String html = processHtmlTemplate(apartmentReport, emailType);

        MimeMessage message = emailSender.createMimeMessage();
        message.setContent(html, "text/html; charset=utf-8");

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        String subject = String.format(TITLE_PATTERN, apartmentReport.getCity().getName());
        helper.setSubject(subject);

        return new MimeMessageWrapper(message, helper);
    }

    private String processHtmlTemplate(ApartmentReport apartmentReport, EmailType emailType) {
        Context context = new Context(Locale.ENGLISH);
        context.setVariable("city", apartmentReport.getCity().getName());
        context.setVariable("report", apartmentReport);

        String templatePath = switch (emailType) {
            case INITIAL -> "email/initial.html";
            case SUBSCRIPTION -> "email/subscription.html";
        };

        return templateEngine.process(templatePath, context);
    }

    private void initialEmail(MimeMessageWrapper messageWrapper, String email) throws MessagingException {
        messageWrapper.helper.setTo(email);
        emailSender.send(messageWrapper.message);
    }

    private void notifySubscribers(MimeMessageWrapper messageWrapper, Set<String> emails) throws MessagingException {
        messageWrapper.helper.setBcc(emails.toArray(new String[0]));
        emailSender.send(messageWrapper.message);
    }

    private record MimeMessageWrapper(MimeMessage message, MimeMessageHelper helper){}

    private enum EmailType {
        INITIAL,
        SUBSCRIPTION
    }
}
