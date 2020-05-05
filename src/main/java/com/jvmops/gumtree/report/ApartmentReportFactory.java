package com.jvmops.gumtree.report;

import com.jvmops.gumtree.Time;
import com.jvmops.gumtree.city.City;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Lazy
@AllArgsConstructor
@Slf4j
class ApartmentReportFactory {
    private static final Sort SORT_BY_PRICE = Sort.by("price").descending();
    private static final Sort SORT_BY_GUMTREE_CREATION_TIME_AND_PRICE = Sort.by("gumtreeCreationDate", "price").descending();


    private AdRepository adRepository;
    private Time time;

    ApartmentReport create(City city) {
        List<Ad> newest = newestAds(city.getName());
        List<Ad> gas = gasApartments(city.getName());
        List<Ad> dishwasher = dishwasherApartments(city.getName());
        List<Ad> dishwasherAndGas = findCommons(gas, dishwasher);
        List<Ad> cheapest = cheapestApartments(city.getName());
        var apartmentReport = ApartmentReport.builder()
                .city(city)
                .newApartments(newest)
                .gasApartments(gas)
                .dishwasherApartments(dishwasher)
                .dishwasherAndGasApartments(dishwasherAndGas)
                .cheapestApartments(cheapest)
                .build();
        log.debug(apartmentReport.toString());
        return apartmentReport;
    }

    private List<Ad> newestAds(String city) {
        LocalDateTime yesterday = time.now().minusDays(1);
        return adRepository.findAllByCityAndCreationTimeGreaterThanOrderByGumtreeCreationDate(city, yesterday);
    }

    private List<Ad> gasApartments(String city) {
        return adRepository.findByCityAndDescriptionContainsAndGumtreeCreationDateGreaterThan(
                city, "gaz", oneWeekAgo(), SORT_BY_GUMTREE_CREATION_TIME_AND_PRICE);
    }

    private List<Ad> dishwasherApartments(String city) {
        return adRepository.findByCityAndDescriptionContainsAndGumtreeCreationDateGreaterThan(
                city, "zmywark",  oneWeekAgo(), SORT_BY_GUMTREE_CREATION_TIME_AND_PRICE);
    }

    private List<Ad> findCommons(List<Ad> gasAds, List<Ad> dishwasherAds) {
        Set<Ad> gasAdsSet = new HashSet<>(gasAds);
        return dishwasherAds.stream()
                .filter(Predicate.not(gasAdsSet::add))
                .collect(Collectors.toList());
    }

    private List<Ad> cheapestApartments(String city) {

        return adRepository.findTop10ByCityAndGumtreeCreationDateGreaterThan(
                city, oneWeekAgo(), SORT_BY_PRICE);
    }

    private LocalDate oneWeekAgo() {
        return time.now().minusWeeks(1).toLocalDate();
    }
}
