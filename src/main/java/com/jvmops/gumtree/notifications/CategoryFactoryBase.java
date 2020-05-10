package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Time;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

abstract class CategoryFactoryBase implements CategoryFactory {
    private static final Sort DEFAULT_SORT = Sort.by(
            desc("gumtreeCreationDate"),
            asc("price"));
    protected static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, 20, DEFAULT_SORT);

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
