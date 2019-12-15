package com.jvmops.gumtree.cities;

import com.jvmops.gumtree.config.City;
import com.jvmops.gumtree.config.ManagedConfiguration;
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
    private ManagedConfiguration managedConfiguration;

    @GetMapping
    public String cities(Model model) {
        Set<CityDto> cities = managedConfiguration.cities().stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
        model.addAttribute("cities", cities);
        model.addAttribute("email", new EmailDto());
        model.addAttribute("cityEmail", new CityEmailDto());

        return "cities";
    }

    private CityDto toDto(City city) {
        return CityDto.builder()
                .name(city.getName())
                .emails(city.getEmails())
                .build();
    }

    @PostMapping
    public String processCityEmail(@Valid CityEmailDto cityEmail) {
        if (StringUtils.isEmpty(cityEmail.getEmail())) {
            managedConfiguration.addCity(cityEmail.getCity());
        } else {
            managedConfiguration.subscribeToNotifications(cityEmail.getCity(), cityEmail.getEmail());
        }
        return "redirect:/cities";
    }

    @PostMapping("emails")
    public String stopNotifications(@Valid EmailDto email) {
        if (!managedConfiguration.stopNotifications(email.getValue())) {
            throw new IllegalStateException(
                    String.format("Email %s was not removed from notifications", email.getValue())
            );
        }
        return "redirect:cities";
    }
}
