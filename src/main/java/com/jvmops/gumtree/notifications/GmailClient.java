package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.EmailTemplateProcessor.EmailWithReport;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
class GmailClient implements NotificationSender {
    private JavaMailSender emailSender;

    @Override
    public void initialEmail(EmailWithReport email, String subscriberWannabe) {
        String city = email.report().getCity().getName();
        String subject = email.report().getTitle();
        try {
            MimeMessageWrapper message = prepareMessage(subject, email.html());
            sendInitialMessage(message, subscriberWannabe);
        } catch (MessagingException e) {
            log.error("Unable to send initial email for {} subscription to {}", city, subscriberWannabe, e);
        }
    }

    @Override
    public void notifySubscribers(EmailWithReport email) {
        String city = email.report().getCity().getName();
        String subject = email.report().getTitle();
        Set<String> subscribers = email.report().getCity().getSubscribers();

        if (isEmpty(subscribers)) {
            // this is handled before
            log.error("No one is subscribed to {} report!", city);
        }

        try {
            MimeMessageWrapper message = prepareMessage(subject, email.html());
            log.info("Sending {} report to: {}}", city, subscribers);
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
