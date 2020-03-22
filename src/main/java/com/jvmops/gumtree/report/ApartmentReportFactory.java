package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.City;
import com.jvmops.gumtree.Time;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Lazy
@AllArgsConstructor
@Slf4j
class ApartmentReportFactory {
    private static final Sort SORT_BY_PRICE = Sort.by("price");

    private AdRepository adRepository;
    private Time time;

    ApartmentReport create(City city) {
        List<Ad> newestAds = newestAds(city.getName());
        List<Ad> gasApartments = gasApartments(city.getName());
        List<Ad> cheapestApartments = cheapestApartments(city.getName());
        log.info("ApartmentReport :: newestAds.size() == {} :: gasApartments.size() == {} :: cheapestApartments.size() == {}",
                newestAds.size(), gasApartments.size(), cheapestApartments.size());
        var apartmentReport = new ApartmentReport(city, newestAds, gasApartments, cheapestApartments);
        log.debug(apartmentReport.toString());
        return apartmentReport;
    }

    private List<Ad> newestAds(String city) {
        LocalDateTime yesterday = time.now().minusDays(1);
        return adRepository.findAllByCityAndCreationTimeGreaterThanOrderByPrice(city, yesterday);
    }

    private List<Ad> gasApartments(String city) {
        return adRepository.findByCityAndDescriptionContains(city, "gaz");
    }

    private List<Ad> cheapestApartments(String city) {
        LocalDate threeDaysAgo = time.now().minusDays(3).toLocalDate();
        return adRepository.findTop10ByCityAndGumtreeCreationDateGreaterThan(city, threeDaysAgo, SORT_BY_PRICE);
    }
}
