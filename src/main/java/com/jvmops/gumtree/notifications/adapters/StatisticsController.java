package com.jvmops.gumtree.notifications.adapters;

import com.jvmops.gumtree.notifications.StatisticsGenerator;
import com.jvmops.gumtree.notifications.model.Statistics;
import com.jvmops.gumtree.subscriptions.CityService;
import com.jvmops.gumtree.subscriptions.model.City;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class StatisticsController {
    private CityService cityService;
    private StatisticsGenerator statisticsGenerator;

    @GetMapping
    public Statistics notifications(@RequestParam("city") String cityName) {
        City city = cityService.getByName(cityName);
        return statisticsGenerator.getStatistics(city);
    }
}
