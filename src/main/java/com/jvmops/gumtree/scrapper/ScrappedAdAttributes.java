package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
class ScrappedAdAttributes {
    private static final String LANDLORD = "Do wynajęcia przez";
    private static final String AVAILABLE_SINCE = "Dostępny od";
    private static final String SIZE = "Wielkość (m2)";
    private static final String CREATION_DATE = "Data dodania";
    private static final DateTimeFormatter CREATION_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Map<String, String> attributes;

    private Optional<String> getOptionalValue(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    LocalDate getCreationDate() {
        return getOptionalValue(CREATION_DATE)
                .map(creationDate -> LocalDate.parse(creationDate, CREATION_DATE_FORMATTER))
                .orElse(null);
    }

    LocalDate getAvailableSince() {
        return getOptionalValue(AVAILABLE_SINCE)
                .map(LocalDate::parse)
                .orElse(null);
    }

    Integer getSize() {
        return getOptionalValue(SIZE)
                .map(Integer::parseInt)
                .orElse(null);
    }

    String getLandlord() {
        return attributes.get(LANDLORD);
    }
}
