package com.jvmops.gumtree.notifications.model;

import com.jvmops.gumtree.subscriptions.City;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@EqualsAndHashCode(of = {"apartmentReportType", "city"})
@Getter
@Slf4j
@Builder
public class ApartmentReport {
    private ApartmentReportType apartmentReportType;
    private boolean empty;
    private City city;
    private List<Category> categories;

    public static ApartmentReport empty(City city, ApartmentReportType apartmentReportType) {
        return ApartmentReport.builder()
                .city(city)
                .apartmentReportType(apartmentReportType)
                .empty(true)
                .build();
    }

    public String getTitle() {
        return String.format(apartmentReportType.titleTemplate,
                city.getName());
    }
}
