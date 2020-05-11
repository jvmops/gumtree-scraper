package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.Ad;
import com.jvmops.gumtree.notifications.model.Category;
import com.jvmops.gumtree.notifications.model.CategoryType;
import com.jvmops.gumtree.notifications.ports.AdRepository;
import com.jvmops.gumtree.notifications.ports.ShameRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;

import static com.jvmops.gumtree.notifications.model.CategoryType.DISHWASHER_ONLY;

@Component
@Lazy
class DishwasherOnlyApartments extends CategoryLoaderBase {
    private ShameRepository regexpAdRepository;

    public DishwasherOnlyApartments(
            ShameRepository regexpAdRepository,
            AdRepository adRepository,
            Clock clock) {
        super(adRepository, clock);
        this.regexpAdRepository = regexpAdRepository;
    }

    @Override
    public CategoryType categoryType() {
        return DISHWASHER_ONLY;
    }

    @Override
    public Category load(String city) {
        List<Ad> ads = regexpAdRepository.findAllByCityWithDishwasherOnly(
                city,
                oneWeekAgo(),
                DEFAULT_PAGE_REQUEST);
        return Category.builder()
                .type(DISHWASHER_ONLY)
                .ads(ads)
                .build();
    }
}
