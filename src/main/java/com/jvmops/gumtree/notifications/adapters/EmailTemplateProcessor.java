package com.jvmops.gumtree.notifications.adapters;

import com.jvmops.gumtree.ScrapperProperties;
import com.jvmops.gumtree.notifications.model.ApartmentReport;
import com.jvmops.gumtree.notifications.model.EmailWithReport;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
class EmailTemplateProcessor {
    private TemplateEngine templateEngine;
    private ScrapperProperties scrapperProperties;

    EmailWithReport initialEmail(ApartmentReport apartmentReport, String email) {
        Context context = initializeContext(apartmentReport);
        context.setVariable("email", encodeForUrl(email));
        String html = processTemplate(context);
        log.debug("Initial email with {} {} apartment report generated", apartmentReport.getCity(), apartmentReport.getApartmentReportType());
        return new EmailWithReport(apartmentReport, html);
    }

    EmailWithReport subscriptionEmail(ApartmentReport apartmentReport) {
        Context context = initializeContext(apartmentReport);
        String html = processTemplate(context);
        log.debug("Subscription email with {} {} apartment report generated", apartmentReport.getCity(), apartmentReport.getApartmentReportType());
        return new EmailWithReport(apartmentReport, html);
    }

    private String processTemplate(Context context) {
        return templateEngine.process("email.html", context);
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
