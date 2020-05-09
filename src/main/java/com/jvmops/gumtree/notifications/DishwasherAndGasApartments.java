package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Time;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
class DishwasherAndGasApartments extends CategoryFactoryBase {

    private SuperQuerasyK regexpAdRepository;

    public DishwasherAndGasApartments(
            SuperQuerasyK regexpAdRepository,
            AdRepository adRepository,
            Time time) {
        super(adRepository, time);
        this.regexpAdRepository = regexpAdRepository;
    }

    @Override
    public CategoryType categoryType() {
        return CategoryType.DISHWASHER_AND_GAS;
    }

    @Override
    public Category of(String city) {
        List<Ad> ads = regexpAdRepository.findAllByCityWithDishwasherAndGas(city, oneWeekAgo());
        return Category.builder()
                .header("Mieszkania ze zmywarką i gazem:")
                .ads(ads)
                .build();
    }
}
