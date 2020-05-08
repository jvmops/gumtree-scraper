package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@RequiredArgsConstructor
abstract class CategoryFactoryBase implements CategoryFactory {
    protected static final Sort SORT_BY_GUMTREE_CREATION_TIME_AND_PRICE = Sort.by("gumtreeCreationDate", "price").ascending();

    protected AdRepository adRepository;
    protected Time time;

    protected LocalDate oneWeekAgo() {
        return time.now().minusWeeks(1).toLocalDate();
    }
}
