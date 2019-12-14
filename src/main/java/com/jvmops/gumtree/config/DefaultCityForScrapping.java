package com.jvmops.gumtree.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Profile("!default")
@Component
@Slf4j
@AllArgsConstructor
class DefaultCityForScrapping {
    private final static MailConfig KATOWICE_SCRAPPER_ON_NOTIFICATIONS_OFF = MailConfig.builder()
            .id(ObjectId.get())
            .emails(Set.of())
            .city("katowice")
            .build();

    private MailConfigRepository mailConfigRepository;

    @PostConstruct
    void addIfNoneSet() {
        if (mailConfigRepository.count() == 0) {
            mailConfigRepository.save(KATOWICE_SCRAPPER_ON_NOTIFICATIONS_OFF);
        }
    }
}
