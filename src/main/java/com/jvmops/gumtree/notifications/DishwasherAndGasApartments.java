package com.jvmops.gumtree.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
@RequiredArgsConstructor
class DishwasherAndGasApartments extends CategoryFactoryBase {
    @Override
    public CategoryType categoryType() {
        return CategoryType.DISHWASHER_AND_GAS;
    }

    @Override
    public Category get(String city) {
        String description = "gaz zmywark";
        List<Ad> ads = adRepository.findByCityAndDescriptionContainsAndGumtreeCreationDateGreaterThan(
                city, description, oneWeekAgo(), SORT_BY_GUMTREE_CREATION_TIME_AND_PRICE);
        return Category.builder()
                .header("Mieszkania ze zmywarką i gazem:")
                .ads(ads)
                .build();
    }
}
