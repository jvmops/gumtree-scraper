package com.jvmops.gumtree.city;

public class CityNotFound extends RuntimeException {
    public CityNotFound(String city) {
        super(String.format("City %s not found", city));
    }
}
