package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.Time;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Lazy
class NewestApartments extends CategoryFactoryBase {
    public NewestApartments(AdRepository adRepository, Time time) {
        super(adRepository, time);
    }

    @Override
    public CategoryType categoryType() {
        return CategoryType.NEWEST;
    }

    @Override
    public Category of(String city) {
        LocalDateTime time = this.time.now().minusHours(2);
        List<Ad> ads = adRepository.findTop20ByCityAndCreationTimeGreaterThanOrderByGumtreeCreationDate(city, time);
        return Category.builder()
                .header("Jeszcze ciepłe:")
                .ads(ads)
                .build();
    }
}
