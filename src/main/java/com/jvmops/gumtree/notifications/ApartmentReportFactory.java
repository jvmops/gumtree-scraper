package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.scrapper.Ad;
import com.jvmops.gumtree.scrapper.AdRepository;
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

    public ApartmentReport create() {
        List<Ad> newestAds = newestAds();
        List<Ad> gasApartments = gasApartments();
        List<Ad> cheapestApartments = cheapestApartments();
        log.info("Creating ApartmentReport :: newestAds.size() == {} :: gasApartments.size() == {} :: cheapestApartments.size() == {}",
                newestAds.size(), gasApartments.size(), cheapestApartments.size());
        ApartmentReport apartmentReport = new ApartmentReport(newestAds, gasApartments, cheapestApartments);
        log.debug(apartmentReport.toString());
        return apartmentReport;
    }

    private List<Ad> newestAds() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        return adRepository.findAllByCreationTimeGreaterThanAndRefreshedFalseOrderByPrice(yesterday);
    }

    private List<Ad> gasApartments() {
        return adRepository.findByDescriptionContains("gaz");
    }

    private List<Ad> cheapestApartments() {
        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        return adRepository.findTop10ByGumtreeCreationDateGreaterThan(threeDaysAgo, SORT_BY_PRICE);
    }
}
