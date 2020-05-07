package com.jvmops.gumtree.subscriptions;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("web")
@Controller
@RequestMapping("/subscriptions")
@Slf4j
@AllArgsConstructor
public class SubscriptionsController {
    private CityService cityService;

    @GetMapping
    public String cities(Model model) {
        Set<CitySubscribers> citySubscribers = cityService.cities().stream()
                .map(this::citySubscribers)
                .collect(Collectors.toSet());
        List<String> cities = citySubscribers.stream()
                .map(CitySubscribers::getCityName)
                .collect(Collectors.toList());
        model.addAttribute("cities", cities);
        model.addAttribute("citySubscribers", citySubscribers);
        model.addAttribute("subscription", new Subscription());
        return "subscriptions";
    }

    @PostMapping
    public String subscribe(@Valid Subscription subscription) {
        cityService.start(subscription);
        return "redirect:/subscriptions";
    }

    private CitySubscribers citySubscribers(City city) {
        return CitySubscribers.builder()
                .cityName(city.getName())
                .emails(city.getSubscribers())
                .build();
    }
}
