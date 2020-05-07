package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.subscriptions.City;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
@Builder
class ApartmentReport {
    private City city;
    private List<Ad> newApartments;
    private List<Ad> gasOnlyApartments;
    private List<Ad> dishwasherOnlyApartments;
    private List<Ad> dishwasherAndGasApartments;
    private List<Ad> cheapestApartments;
}
