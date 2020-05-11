package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.Ad;
import com.jvmops.gumtree.notifications.model.Category;
import com.jvmops.gumtree.notifications.model.CategoryType;
import com.jvmops.gumtree.notifications.ports.AdRepository;
import org.joda.money.Money;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.List;

import static com.jvmops.gumtree.ScrapperConfig.DEFAULT_CURRENCY;
import static com.jvmops.gumtree.notifications.model.CategoryType.CHEAPEST;

@Component
@Lazy
class CheapestApartments extends CategoryLoaderBase {
    // in order to avoid short term rental ads
    private static final Money MINIMUM_PRICE = Money.of(DEFAULT_CURRENCY, 300);
    private static final Sort SORT_BY_PRICE = Sort.by("price");

    public CheapestApartments(AdRepository adRepository, Clock clock) {
        super(adRepository, clock);
    }

    @Override
    public CategoryType categoryType() {
        return CHEAPEST;
    }

    @Override
    public Category load(String city) {
        List<Ad> ads = adRepository.findTop20ByCityAndPriceGreaterThanAndGumtreeCreationDateGreaterThan(
                city,
                MINIMUM_PRICE,
                oneWeekAgo(),
                SORT_BY_PRICE);

        return Category.builder()
                .type(CHEAPEST)
                .ads(ads)
                .build();
    }
}
