package com.jvmops.gumtree.notifications;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;

import static com.jvmops.gumtree.notifications.CategoryType.DISHWASHER_AND_GAS;

@Component
@Lazy
class DishwasherAndGasApartments extends CategoryFactoryBase {
    private SuperQuerasyK regexpAdRepository;

    public DishwasherAndGasApartments(
            SuperQuerasyK regexpAdRepository,
            AdRepository adRepository,
            Clock clock) {
        super(adRepository, clock);
        this.regexpAdRepository = regexpAdRepository;
    }

    @Override
    public CategoryType categoryType() {
        return DISHWASHER_AND_GAS;
    }

    @Override
    public Category of(String city) {
        List<Ad> ads = regexpAdRepository.findAllByCityWithDishwasherAndGas(
                city,
                oneWeekAgo(),
                DEFAULT_PAGE_REQUEST);
        return Category.builder()
                .type(DISHWASHER_AND_GAS)
                .ads(ads)
                .build();
    }
}
