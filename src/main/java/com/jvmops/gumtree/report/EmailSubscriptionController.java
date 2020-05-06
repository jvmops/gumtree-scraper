package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.Subscription;
import com.jvmops.gumtree.city.CityService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Profile("web")
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class EmailSubscriptionController {
    private CityService cityService;

    @GetMapping("/subscribe")
    public String subscribe(
            @RequestParam String city,
            @RequestParam @Email String email
    ) {
        cityService.subscribeToNotifications(city, email);
        return String.format("redirect:/?subscribed=true&city=%s&email=%s", encodeForUrl(city), encodeForUrl(email));
    }

    @GetMapping("/unsubscribe")
    public String unsubscribeForm(
            @RequestParam String city,
            Model model
    ) {
        Subscription subscription = Subscription.builder()
                .city(city)
                .build();
        model.addAttribute("subscription", subscription);
        return "unsubscribe";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@Valid Subscription subscription) {
        cityService.cancel(subscription);
        return "redirect:/?unsubscribed=true";
    }

    public static String encodeForUrl(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(String.format("Unable to encode value '%s' to url", value));
        }
    }
}
