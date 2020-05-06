package com.jvmops.gumtree.city;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("web")
@Controller
@RequestMapping("/cities")
@Slf4j
@AllArgsConstructor
public class CitiesController {
    private CityService cityService;

    @GetMapping
    public String cities(Model model) {
        Set<CityDto> cities = cityService.cities().stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
        model.addAttribute("cities", cities);
        model.addAttribute("email", new EmailDto());
        model.addAttribute("subscription", new Subscription());
        return "cities";
    }

    @PostMapping
    public String subscribe(@Valid Subscription subscription) {
        cityService.start(subscription);
        return "redirect:/cities";
    }

    private CityDto toDto(City city) {
        return CityDto.builder()
                .name(city.getName())
                .emails(city.getSubscribers())
                .build();
    }
}
