package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.CityService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            @RequestParam String email
    ) {
        cityService.subscribeToNotifications(city, email);
        return String.format("redirect:/?subscribed=true&city=%s&email=%s", encodeForUrl(city), encodeForUrl(email));
    }

    @GetMapping("/unsubscribe")
    public String unsubscribe(
            @RequestParam String city,
            @RequestParam String email
    ) {
        return String.format("redirect:/?unsubscribed=true&city=%s&email=%s", encodeForUrl(city), encodeForUrl(email));
    }

    public static String encodeForUrl(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(String.format("Unable to encode value '%s' to url", value));
        }
    }
}
