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
    private ReportType reportType;
    private boolean empty;
    private String title;
    private City city;
    private List<Category> categories;

    static ApartmentReport empty() {
        return ApartmentReport.builder()
                .empty(true)
                .build();
    }
}
