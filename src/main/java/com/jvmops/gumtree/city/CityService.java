package com.jvmops.gumtree.city;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Configuration
@Slf4j
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public Set<City> cities() {
        return findAll().collect(Collectors.toSet());
    }

    public Set<String> emails(String city) {
        return findCityByName(city)
                .getEmails();
    }

    public City findCityByName(String city) {
        return cityRepository.findByName(city)
                .orElseThrow(() -> new CityNotFound(city));
    }

    public City addCity(String name) {
        return cityRepository.save(
                createNewCity(name, null)
        );
    }

    public City subscribeToNotifications(String city, String email) {
        log.info("Subscribing {} to {} notifications...", email, city);
        City toBeSaved = cityRepository.findByName(city)
                .map(existingCity -> subscribe(email, existingCity))
                .orElse(createNewCity(city, email));
        return cityRepository.save(toBeSaved);
    }

    @SuppressWarnings("squid:S3864")
    public void stopNotifications(String email) {
        Set<City> cities = cityRepository.findAllByEmailsContaining(email);
        cities.stream()
                .peek(city -> log.info("Removing {} from {} notifications", email, city.getName()))
                .forEach(city -> city.getEmails().remove(email));
        cityRepository.saveAll(cities);
    }

    private Stream<City> findAll() {
        var iterable = cityRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private City subscribe(String email, City existingCity) {
        boolean subscribed = existingCity.subscribe(email);
        if (!subscribed) {
            log.warn("{} IS ALREADY ON the notification list for {}", email, existingCity.getName());
        }
        return existingCity;
    }

    private City createNewCity(String city, String email) {
        Set<String> emails = StringUtils.isEmpty(email) ? Set.of() : Set.of(email);
        return City.builder()
                .id(ObjectId.get())
                .name(city)
                .emails(emails)
                .build();
    }
}


