package com.jvmops.gumtree.config;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Configuration
@Slf4j
public class ManagedConfiguration {

    @Autowired
    private CityRepository cityRepository;

    public Set<String> getCities() {
        return emailsByCities().keySet();
    }

    public Set<String> getEmails(String cityName) {
        List<String> emailsList = emailsByCities().get(cityName);
        if (emailsList == null) {
            emailsList = List.of();
        }
        log.info("{} apartments watch-list: {}", cityName, emailsList);
        return new HashSet<>(emailsList);
    }

    public boolean stopNotifications(String email) {
//      TODO: cityRepository.findAllByEmail and remove
        return false;
    }

    public Set<City> cities() {
        Set<City> cities = new HashSet<>();
        cityRepository.findAll()
                .forEach(cities::add);
        return cities;
    }

    private MultiValueMap<String, String> emailsByCities() {
        var emailsByCities = new LinkedMultiValueMap<String, String>();
        findAll().forEach(city ->
                // TODO: maybe implement a collector! :)?
                emailsByCities.addAll(
                        city.getName(),
                        city.getEmailsAsList()
                )
        );
        return emailsByCities;
    }

    private Stream<City> findAll() {
        var iterable = cityRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public City addCity(String name) {
        City newCity = City.builder()
                .id(ObjectId.get())
                .name(name)
                .emails(Set.of())
                .build();
        return cityRepository.save(newCity);
    }

    public City subscribeToNotifications(String city, String email) {
        log.info("Subscribing {} to {} notifications...", email, city);
        City toBeSaved = cityRepository.findByName(city)
                .map(existingCity -> {
                    boolean subscribed = existingCity.subscribe(email);
                    if (!subscribed) {
                        log.warn("{} IS ALREADY ON the notification list for {}", email, city);
                    }
                    return existingCity;
                })
                .orElse(createNewCity(city, email));
        return cityRepository.save(toBeSaved);
    }

    private City createNewCity(String city, String email) {
        return City.builder()
                .id(ObjectId.get())
                .name(city)
                .emails(Set.of(email))
                .build();
    }
}

@Repository
interface CityRepository extends CrudRepository<City, Long> {
    Optional<City> findByName(String city);
}


