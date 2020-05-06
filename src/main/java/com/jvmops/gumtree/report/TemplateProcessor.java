package com.jvmops.gumtree.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

import static com.jvmops.gumtree.report.EmailSubscriptionController.encodeForUrl;

@Lazy
@Component
@Slf4j
@RequiredArgsConstructor
public class TemplateProcessor {
    private final TemplateEngine templateEngine;

    String initialEmail(ApartmentReport apartmentReport, String email) {
        Context context = initializeContext(apartmentReport);
        context.setVariable("baseUrl", ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
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
        context.setVariable("cityForUrl", encodeForUrl(apartmentReport.getCity().getName()));
        return context;
    }
}
