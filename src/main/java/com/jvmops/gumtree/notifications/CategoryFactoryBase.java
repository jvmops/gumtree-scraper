package com.jvmops.gumtree.notifications;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

abstract class CategoryFactoryBase implements CategoryFactory {
    private static final Sort DEFAULT_SORT = Sort.by(
            desc("gumtreeCreationDate"),
            asc("price"));
    protected static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, 20, DEFAULT_SORT);

    protected AdRepository adRepository;
    protected Clock clock;

    public CategoryFactoryBase(AdRepository adRepository, Clock clock) {
        this.adRepository = adRepository;
        this.clock = clock;
    }

    protected LocalDate oneWeekAgo() {
        return LocalDate.now(clock)
                .minusWeeks(1);
    }
}
