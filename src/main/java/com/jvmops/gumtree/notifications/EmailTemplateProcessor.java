package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.ScrapperProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Lazy
@Component
@Slf4j
@RequiredArgsConstructor
public class EmailTemplateProcessor {
    private final TemplateEngine templateEngine;
    private final ScrapperProperties scrapperProperties;

    String initialEmail(ApartmentReport apartmentReport, String email) {
        Context context = initializeContext(apartmentReport);

        context.setVariable("email", encodeForUrl(email));
        return templateEngine.process("email/initial.html", context);
    }

    String subscriptionEmail(ApartmentReport apartmentReport) {
        Context context = initializeContext(apartmentReport);
        return templateEngine.process("email/subscription.html", context);
    }

    private Context initializeContext(ApartmentReport apartmentReport) {
        Context context = new Context(Locale.ENGLISH);
        context.setVariable("city", apartmentReport.getCity().getName());
        context.setVariable("report", apartmentReport);
        context.setVariable("baseUrl", scrapperProperties.getWebsiteUrl());
        context.setVariable("cityForUrl", encodeForUrl(apartmentReport.getCity().getName()));
        return context;
    }

    public static String encodeForUrl(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(String.format("Unable to encode value '%s' to url", value));
        }
    }
}
