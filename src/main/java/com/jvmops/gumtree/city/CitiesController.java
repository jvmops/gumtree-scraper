package com.jvmops.gumtree.city;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Profile("web")
@Controller
@RequestMapping("/cities")
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
        model.addAttribute("cityEmail", new CityEmailDto());

        return "cities";
    }

    @PostMapping
    public String processCityEmail(@Valid CityEmailDto cityEmail) {
        if (StringUtils.isEmpty(cityEmail.getEmail())) {
            cityService.addCity(cityEmail.getCity());
        } else {
            cityService.subscribeToNotifications(cityEmail.getCity(), cityEmail.getEmail());
        }
        return "redirect:/cities";
    }

    @PostMapping("emails")
    public String stopNotifications(@Valid EmailDto email) {
        cityService.stopNotifications(email.getValue());
        return "redirect:cities";
    }

    private CityDto toDto(City city) {
        return CityDto.builder()
                .name(city.getName())
                .emails(city.getEmails())
                .build();
    }
}
