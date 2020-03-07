package com.jvmops.gumtree;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component("customTime")
public class CustomTime implements Time {
    private static final LocalDate POLAND_INDEPENDENCE_DAY = LocalDate.parse("2019-11-11");

    @Override
    public LocalDateTime now() {
        return POLAND_INDEPENDENCE_DAY.atTime(LocalTime.parse("20:00"));
    }
}
