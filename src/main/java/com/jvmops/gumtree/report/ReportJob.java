package com.jvmops.gumtree.report;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("report")
@Component
@AllArgsConstructor
public class ReportJob {
    private ReportService reportService;

    @PostConstruct
    void execute() {
        reportService.notifySubscribers();
    }
}
