package com.jvmops.gumtree.notifications;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Lazy
@RequiredArgsConstructor
class NewestApartments extends CategoryFactoryBase {
    static final int PERIOD = 2;

    @Override
    public CategoryType categoryType() {
        return CategoryType.NEWEST;
    }

    @Override
    public Category get(String city) {
        LocalDateTime time = this.time.now().minusHours(PERIOD);
        List<Ad> ads = adRepository.findTop20ByCityAndCreationTimeGreaterThanOrderByGumtreeCreationDate(city, time);
        return Category.builder()
                .header("Jeszcze ciepłe:")
                .ads(ads)
                .build();
    }
}
