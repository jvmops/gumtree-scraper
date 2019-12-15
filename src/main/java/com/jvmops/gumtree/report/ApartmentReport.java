package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
@AllArgsConstructor
class ApartmentReport {
    private static final String APARTMENT = "%s :: %s :: %sm2 - %szł :: %s :: refreshed %s times";
    private static final String TWO_LINES = "\n\n";

    private City city;
    private List<Ad> newApartments;
    private List<Ad> gasApartments;
    private List<Ad> cheapestApartments;

    public String getReport() {
        return toString("First seen ads:", newApartments)
                + toString("Apartments with gas:", gasApartments)
                + toString("Cheapest apartments:", cheapestApartments);
    }

    private String toString(String headline, List<Ad> ads) {
        String formattedAds = ads.stream()
                .map(this::toString)
                .collect(Collectors.joining("\n"));
        return TWO_LINES + headline + TWO_LINES + formattedAds;
    }

    private String toString(Ad ad) {
        return String.format(APARTMENT, ad.getGumtreeCreationDate(), ad.getLandlord(), ad.getSize(), ad.getPrice(),  ad.getTitle(), ad.getUpdates().size());
    }
}
