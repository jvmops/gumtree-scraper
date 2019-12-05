package com.jvmops.gumtree.report;

import com.jvmops.gumtree.config.Time;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ApartmentReportFactory {
    private static final Sort SORT_BY_PRICE = Sort.by("price");

    private AdRepository adRepository;
    private Time time;

    public ApartmentReport create(String city) {
        List<Ad> newestAds = newestAds(city);
        List<Ad> gasApartments = gasApartments(city);
        List<Ad> cheapestApartments = cheapestApartments(city);
        log.info("Creating {} ApartmentReport :: newestAds.size() == {} :: gasApartments.size() == {} :: cheapestApartments.size() == {}",
                city, newestAds.size(), gasApartments.size(), cheapestApartments.size());
        var apartmentReport = new ApartmentReport(city, newestAds, gasApartments, cheapestApartments);
        log.debug(apartmentReport.toString());
        return apartmentReport;
    }

    // TODO implement city
    private List<Ad> newestAds(String city) {
        LocalDateTime yesterday = time.now().minusDays(1);
        return adRepository.findAllByCreationTimeGreaterThanAndRefreshedFalseOrderByPrice(yesterday);
    }

    // TODO implement city
    private List<Ad> gasApartments(String city) {
        return adRepository.findByDescriptionContains("gaz");
    }

    // TODO implement city
    private List<Ad> cheapestApartments(String city) {
        LocalDate threeDaysAgo = time.now().minusDays(3).toLocalDate();
        return adRepository.findTop10ByGumtreeCreationDateGreaterThan(threeDaysAgo, SORT_BY_PRICE);
    }
}
