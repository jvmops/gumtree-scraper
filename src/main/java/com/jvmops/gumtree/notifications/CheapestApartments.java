package com.jvmops.gumtree.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy
@RequiredArgsConstructor
class CheapestApartments extends CategoryFactoryBase {
    private static final Sort SORT_BY_PRICE = Sort.by("price");

    @Override
    public CategoryType categoryType() {
        return CategoryType.CHEAPEST;
    }

    @Override
    public Category get(String city) {
        List<Ad> ads = adRepository.findTop20ByCityAndPriceGreaterThanAndGumtreeCreationDateGreaterThan(
                city, 0, oneWeekAgo(), SORT_BY_PRICE);

        return Category.builder()
                .header("Najtańsze oferty")
                .ads(ads)
                .build();
    }
}
