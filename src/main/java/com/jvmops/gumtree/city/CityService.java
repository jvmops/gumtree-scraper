package com.jvmops.gumtree.city;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class CityService {

    private final CityRepository cityRepository;

    public Set<City> cities() {
        return findAll().collect(Collectors.toSet());
    }

    public Set<String> emails(String city) {
        return findCityByName(city)
                .getSubscribers();
    }

    public City findCityByName(String city) {
        return cityRepository.findByNameIgnoreCase(city)
                .orElseThrow(() -> new CityNotFound(city));
    }

    public City addCity(String name, String urlCode) {
        return cityRepository.save(
                createNewCity(name, "urlCode")
        );
    }

    public City subscribeToNotifications(String cityName, String email) {
        log.info("Subscribing {} to {} notifications...", email, cityName);
        City toBeSaved = cityRepository.findByNameIgnoreCase(cityName)
                .map(existingCity -> subscribe(email, existingCity))
                .orElseThrow(() -> new CityNotFound(cityName));
        return cityRepository.save(toBeSaved);
    }

    @SuppressWarnings("squid:S3864")
    public void stopNotifications(String email) {
        Set<City> cities = cityRepository.findAllBySubscribersContaining(email);
        cities.stream()
                .peek(city -> log.info("Removing {} from {} notifications", email, city.getName()))
                .forEach(city -> city.getSubscribers().remove(email));
        cityRepository.saveAll(cities);
    }

    private Stream<City> findAll() {
        var iterable = cityRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    private City subscribe(String email, City city) {
        boolean subscribed = city.subscribe(email);
        if (!subscribed) {
            log.warn("{} IS ALREADY ON the notification list for {}", email, city.getName());
        }
        return city;
    }

    private City createNewCity(String city, String urlCode) {
        return City.builder()
                .id(ObjectId.get())
                .name(city)
                .urlCode(urlCode)
                .build();
    }

    public City cancel(Subscription subscription) {
        City city = cityRepository.findByNameIgnoreCase(subscription.getCity())
                .orElseThrow(() -> new CityNotFound(subscription.getCity()));

        boolean unsubscribed = city.unsubscribe(subscription.getEmail());
        if (unsubscribed) {
            log.info("{} has been unsubscribed from {} report", subscription.getEmail(), city.getName());
            cityRepository.save(city);
        } else {
            log.warn("There is no such email as {} among {} subscribers", subscription.getEmail(), city.getName());
        }

        return city;
    }
}


