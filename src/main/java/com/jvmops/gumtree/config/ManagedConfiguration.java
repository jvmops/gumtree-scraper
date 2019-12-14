package com.jvmops.gumtree.config;

import com.mongodb.lang.Nullable;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Configuration
@Slf4j
public class ManagedConfiguration {

    @Autowired
    private MailConfigRepository mailConfigRepository;

    public Set<String> getCities() {
        return emailsByCities().keySet();
    }

    public Set<String> getEmails(String city) {
        List<String> emailsList = emailsByCities().get(city);
        if (emailsList == null) {
            emailsList = List.of();
        }
        log.info("{} apartments watch-list: {}", city, emailsList);
        return new HashSet<>(emailsList);
    }

    private MultiValueMap<String, String> emailsByCities() {
        var emailsByCities = new LinkedMultiValueMap<String, String>();
        findAll().forEach(mailConfig ->
                // TODO: maybe implement a collector! :)?
                emailsByCities.addAll(
                        mailConfig.getCity(),
                        mailConfig.getEmailsAsList()
                )
        );
        return emailsByCities;
    }

    private Stream<MailConfig> findAll() {
        var iterable = mailConfigRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private void initializeCollection() {
        mailConfigRepository.saveAll(
                Set.of(katowice())
        );
    }

    private static MailConfig katowice() {
        return MailConfig.builder()
                .id(ObjectId.get())
                .city("katowice")
                .emails(Set.of("jvmops+default@gmail.com"))
                .build();
    }
}

@Repository
interface MailConfigRepository extends CrudRepository<MailConfig, Long> {
}

@Document
@Value
@Builder
class MailConfig {
    @Id
    private ObjectId id;
    private String city;
    @Nullable
    private Set<String> emails;
    @CreatedDate
    private LocalDateTime creationTime;

    public List<String> getEmailsAsList() {
        return new ArrayList<>(emails);
    }
}
