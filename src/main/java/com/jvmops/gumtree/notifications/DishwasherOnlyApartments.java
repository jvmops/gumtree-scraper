package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Time;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

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
        return CategoryType.DISHWASHER_ONLY;
    }

    @Override
    public Category of(String city) {
        List<Ad> ads = regexpAdRepository.findAllByCityWithDishwasherOnly(
                city, oneWeekAgo());
        return Category.builder()
                .header("Mieszkania ze zmywarką:")
                .ads(ads)
                .build();
    }
}
