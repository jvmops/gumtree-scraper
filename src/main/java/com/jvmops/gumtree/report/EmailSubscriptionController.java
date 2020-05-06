package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.CityService;
import com.jvmops.gumtree.city.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Profile("web")
@Controller
@Validated
@RequestMapping("/")
@AllArgsConstructor
public class EmailSubscriptionController {
    private CityService cityService;

    @GetMapping("/subscribe")
    public String subscribe(
            @RequestParam @NotEmpty String city,
            @RequestParam @Email String email,
            Model model
    ) {
        Subscription subscription = Subscription.builder()
                .city(city)
                .email(email)
                .build();
        cityService.start(subscription);
        model.addAttribute("subscription", subscription);
        return "redirect:/?subscribed";
    }

    @GetMapping("/unsubscribe")
    public String unsubscribeForm(
            @RequestParam @NotEmpty String city,
            Model model
    ) {
        Subscription subscription = Subscription.builder()
                .city(city)
                .build();
        model.addAttribute("subscription", subscription);
        return "unsubscribe";
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(
            @Valid Subscription subscription,
            Model model) {
        cityService.cancel(subscription);
        model.addAttribute("subscription", subscription);
        return "redirect:/?unsubscribed";
    }
}
