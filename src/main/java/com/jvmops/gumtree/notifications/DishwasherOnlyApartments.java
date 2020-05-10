package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Time;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.jvmops.gumtree.notifications.CategoryType.DISHWASHER_ONLY;

@Component
@Lazy
class DishwasherOnlyApartments extends CategoryFactoryBase {
    private SuperQuerasyK regexpAdRepository;

    public DishwasherOnlyApartments(
            SuperQuerasyK regexpAdRepository,
            AdRepository adRepository,
            Time time) {
        super(adRepository, time);
        this.regexpAdRepository = regexpAdRepository;
    }

    @Override
    public CategoryType categoryType() {
        return DISHWASHER_ONLY;
    }

    @Override
    public Category of(String city) {
        List<Ad> ads = regexpAdRepository.findAllByCityWithDishwasherOnly(
                city, oneWeekAgo());
        return Category.builder()
                .type(DISHWASHER_ONLY)
                .ads(ads)
                .build();
    }
}
