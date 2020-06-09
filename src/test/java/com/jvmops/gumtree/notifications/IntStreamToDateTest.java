package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.CustomClock;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class IntStreamToDateTest {
    private static final Clock FIXED_CLOCK = new CustomClock().fixedClock();
    private static final LocalDate TODAY = LocalDate.now(FIXED_CLOCK);
    private static final LocalDate _30_DAYS_AGO = TODAY.minusDays(30);

    StatisticsGenerator statisticsGenerator = new StatisticsGenerator(null, FIXED_CLOCK);

    @Test
    void today_is_2020_05_07() {
        Assert.assertEquals(LocalDate.parse("2020-05-07"), TODAY);
    }

    @Test
    void generated_list_should_start_30_days_ago() {
        List<LocalDate> daysSince31DaysAgo = listWithDatesIncrementedBy1ThatEndsOnYesterday();
        var startingDate = daysSince31DaysAgo.get(0);
        Assert.assertEquals(_30_DAYS_AGO, startingDate);
    }

    @Test
    void generated_list_should_end_with_yesterday() {
        List<LocalDate> daysSince30DaysAgo = listWithDatesIncrementedBy1ThatEndsOnYesterday();
        LocalDate endDate = daysSince30DaysAgo.get(daysSince30DaysAgo.size() - 1);
        Assert.assertEquals(LocalDate.parse("2020-05-06"), endDate);
    }

    private List<LocalDate> listWithDatesIncrementedBy1ThatEndsOnYesterday() {
        return statisticsGenerator.datesIncrementedBy1ThatEndsOnYesterday()
                .collect(Collectors.toList());
    }
}
