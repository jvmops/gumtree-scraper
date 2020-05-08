package com.jvmops.gumtree.notifications;

import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Lazy
@AllArgsConstructor
class DishwasherOnlyApartments extends CategoryFactoryBase {
    private CategoryFactory dishwasherAndGasApartments;

    @Override
    public CategoryType categoryType() {
        return CategoryType.DISHWASHER_ONLY;
    }

    @Override
    public Category get(String city) {
        List<ObjectId> dishwasherAndGasAdIds = dishwasherAndGasApartments.get(city)
                .getAds()
                .stream()
                .map(Ad::getId)
                .collect(Collectors.toList());

        String description = "zmywark";
        List<Ad> ads = adRepository.findByCityAndDescriptionContainsAndGumtreeCreationDateGreaterThanAndIdNotIn(
                city, description, oneWeekAgo(), dishwasherAndGasAdIds, SORT_BY_GUMTREE_CREATION_TIME_AND_PRICE);

        return Category.builder()
                .header("Mieszkania ze zmywarką:")
                .ads(ads)
                .build();
    }
}
