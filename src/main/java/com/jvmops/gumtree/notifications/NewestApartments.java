package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.Ad;
import com.jvmops.gumtree.notifications.model.Category;
import com.jvmops.gumtree.notifications.model.CategoryType;
import com.jvmops.gumtree.notifications.ports.AdRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static com.jvmops.gumtree.notifications.model.CategoryType.NEWS;

@Component
@Lazy
class NewestApartments extends CategoryLoaderBase {
    public NewestApartments(AdRepository adRepository, Clock clock) {
        super(adRepository, clock);
    }

    @Override
    public CategoryType categoryType() {
        return NEWS;
    }

    @Override
    public Category load(String city) {
        LocalDateTime twoHoursAgo = LocalDateTime.now(clock)
                .minusHours(2);
        List<Ad> ads = adRepository.findTop20ByCityAndCreationTimeGreaterThanOrderByGumtreeCreationDate(city, twoHoursAgo);
        return Category.builder()
                .type(NEWS)
                .ads(ads)
                .build();
    }
}
