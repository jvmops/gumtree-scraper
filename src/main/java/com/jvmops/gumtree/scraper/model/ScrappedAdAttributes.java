package com.jvmops.gumtree.scraper.model;

import lombok.AllArgsConstructor;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class ScrappedAdAttributes {
    private class AttributeNames {
        static final String LANDLORD = "Do wynajęcia przez";
        static final String AVAILABLE_SINCE = "Dostępny od";
        static final String SIZE = "Wielkość (m2)";
        static final String CREATION_DATE = "Data dodania";
    }

    private static final DateTimeFormatter CREATION_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Map<String, String> attributes;

    public LocalDate getCreationDate() {
        return getOptionalValue(AttributeNames.CREATION_DATE)
                .map(this::parseDate)
                .orElse(LocalDate.now());
    }

    private LocalDate parseDate(String date) {
        return parseDate(date, LocalDate.now());
    }

    private LocalDate parseDate(String date, LocalDate defaultValue) {
        try {
            return LocalDate.parse(date, CREATION_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    public LocalDate getAvailableSince() {
        return getOptionalValue(AttributeNames.AVAILABLE_SINCE)
                .map(date -> parseDate(date, null))
                .orElse(null);
    }

    public Integer getSize() {
        return getOptionalValue(AttributeNames.SIZE)
                .map(Integer::parseInt)
                .orElse(null);
    }

    public String getLandlord() {
        return attributes.get(AttributeNames.LANDLORD);
    }

    private Optional<String> getOptionalValue(String key) {
        return Optional.ofNullable(attributes.get(key));
    }
}
