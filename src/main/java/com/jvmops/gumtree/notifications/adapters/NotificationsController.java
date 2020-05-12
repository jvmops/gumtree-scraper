package com.jvmops.gumtree.notifications.adapters;

import com.jvmops.gumtree.notifications.NotificationService;
import com.jvmops.gumtree.notifications.model.ApartmentReportType;
import com.jvmops.gumtree.subscriptions.CityService;
import com.jvmops.gumtree.subscriptions.model.City;
import com.jvmops.gumtree.subscriptions.model.Subscription;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Profile("web")
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class NotificationsController {
    private NotificationService notificationService;
    private CityService cityService;

    /**
     * its a "home" view
     */
    @GetMapping
    public String notifications(Model model) {
        model.addAttribute("subscription", new Subscription());
        List<String> cityNames = cityService.cities().stream()
                .map(City::getName)
                .collect(Collectors.toList());
        model.addAttribute("cities", cityNames);
        return "notifications";
    }

    /**
     * form with city dropdown and email input
     */
    @PostMapping
    public String initialEmail(
            @Valid Subscription subscription,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "notifications";
        }

        City city = cityService.getByName(subscription.getCity());
        notificationService.initialEmail(
                city, subscription.getEmail());
        return "redirect:/?initialEmailSent";
    }

    /**
     * form with only "Send Report" button on admin view
     */
    @PostMapping("notify")
    public String notifySubscribers() {
        notificationService.notifySubscribers(ApartmentReportType.NEWEST);
        return "redirect:/subscriptions?reportSent";
    }
}
