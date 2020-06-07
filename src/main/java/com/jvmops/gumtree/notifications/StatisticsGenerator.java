package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.SimpleAd;
import com.jvmops.gumtree.notifications.model.Statistics;
import com.jvmops.gumtree.notifications.ports.StatisticsRepository;
import com.jvmops.gumtree.subscriptions.model.City;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatisticsGenerator {
    private static final Sort BY_CREATION_TIME = Sort.by("creationTime");

    private StatisticsRepository adRepository;
    private Clock clock;

    public Statistics getStatistics(City city) {
        LocalDateTime now = LocalDateTime.now(clock);
        var oneMonthAgo = now.minusMonths(1);
        // TODO: scrap more data from ads in order to enhance this
        // TODO: total unique ads per day // new ads chart
        List<SimpleAd> ads = adRepository.findAllByCityAndCreationTimeGreaterThan(city.getName(), oneMonthAgo, BY_CREATION_TIME);
        return Statistics.builder()
                .newPerDay(countPerDay(ads, now.toLocalDate().toString()))
                .build();
    }

    private Map<String, Long> countPerDay(List<SimpleAd> ads, String today) {
        return ads.stream()
                .filter(ad -> !ad.getCreationTimeAsString().equals(today))
                .collect(Collectors.groupingBy(
                        SimpleAd::getCreationTimeAsString,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));
    }
}
