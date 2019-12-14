package com.jvmops.gumtree.config;

import com.jvmops.gumtree.MongoTest;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Slf4j
abstract class DataInitializer extends MongoTest {

    @Autowired
    private MailConfigRepository mailConfigRepository;
    @Autowired
    protected Time time;

    @BeforeAll
    public static void clearData() {
        deleteAll();
    }

    @BeforeEach
    private void insertDataIfNecessary() {
        if (mailConfigRepository.count() == 0) {
            mailConfigRepository.saveAll(mapEmailsToCities());
        }
    }

    private List<MailConfig> mapEmailsToCities() {
        MailConfig katowice = MailConfig.builder()
                .id(ObjectId.get())
                .city("katowice")
                .emails(Set.of("jvmops+test1@gmail.com", "jvmops+test2e@gmail.com"))
                .creationTime(time.now())
                .build();
        MailConfig wroclaw = MailConfig.builder()
                .id(ObjectId.get())
                .city("wroclaw")
                .emails(Set.of())
                .creationTime(time.now())
                .build();
        return List.of(katowice, wroclaw);
    }
}
