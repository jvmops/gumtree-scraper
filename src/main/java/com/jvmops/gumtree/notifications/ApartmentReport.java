package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.subscriptions.City;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@EqualsAndHashCode(of = {"reportType", "city"})
@Getter
@Slf4j
@Builder
class ApartmentReport {
    private ReportType reportType;
    private boolean empty;
    private City city;
    private List<Category> categories;

    static ApartmentReport empty(City city, ReportType reportType) {
        return ApartmentReport.builder()
                .city(city)
                .reportType(reportType)
                .empty(true)
                .build();
    }

    String getTitle() {
        return String.format(reportType.getTitleTemplate(), city.getName());
    }
}
