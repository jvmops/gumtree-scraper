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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class StatisticsGenerator {
    private static final Sort BY_CREATION_TIME = Sort.by("creationTime");

    private StatisticsRepository adRepository;
    private Clock clock;

    public Statistics getStatistics(City city) {
        var _30daysAgo = LocalDateTime.now(clock).minusDays(30);
        // TODO: scrap more data from ads in order to enhance this
        // TODO: total unique ads per day // new ads chart
        List<SimpleAd> ads = adRepository.findAllByCityAndCreationTimeGreaterThan(city.getName(), _30daysAgo, BY_CREATION_TIME);
        return Statistics.builder()
                .newPerDay(countPerDay(ads))
                .build();
    }

    private Map<LocalDate, Integer> countPerDay(List<SimpleAd> ads) {
        LinkedHashMap<LocalDate, Long> perDayCount = ads.stream()
                .collect(Collectors.groupingBy(
                        SimpleAd::getGumtreeCreationDate,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        // I don't want to have today's data on a chart and that is why I am skipping 1 day at the end
        return datesIncrementedBy1ThatEndsOnYesterday()
                .collect(Collectors.toMap(
                        Function.identity(),
                        date -> getCount(date, perDayCount),
                        (k, v) -> { throw new IllegalStateException(String.format("Duplicate key: %s", k)); },
                        LinkedHashMap::new
                ));
    }

    /**
     * package protected for test purposes
     * @see IntStreamToDateTest
     */
    Stream<LocalDate> datesIncrementedBy1ThatEndsOnYesterday() {
        LocalDate _30daysAgo =  LocalDate.now(clock).minusDays(30);
        return IntStream.range(0, 30)
                .mapToObj(_30daysAgo::plusDays);
    }

    /**
     * There are days without any new ad - I want this to be more clear on a chart
     */
    private static Integer getCount(LocalDate date, LinkedHashMap<LocalDate, Long> perDayCount) {
        Long count = perDayCount.get(date);
        if (count == null) {
            return 0;
        } else {
            return count.intValue();
        }
    }
}
