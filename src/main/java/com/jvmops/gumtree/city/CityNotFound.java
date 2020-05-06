package com.jvmops.gumtree.city;

public class CityNotFound extends RuntimeException {
    public CityNotFound(String cityName) {
        super(String.format("City %s not found", cityName));
    }
}
