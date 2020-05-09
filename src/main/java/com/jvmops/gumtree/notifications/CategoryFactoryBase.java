package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Time;

import java.time.LocalDate;

abstract class CategoryFactoryBase implements CategoryFactory {
    protected AdRepository adRepository;
    protected Time time;

    public CategoryFactoryBase(AdRepository adRepository, Time time) {
        this.adRepository = adRepository;
        this.time = time;
    }

    protected LocalDate oneWeekAgo() {
        return time.now().minusWeeks(1).toLocalDate();
    }
}
