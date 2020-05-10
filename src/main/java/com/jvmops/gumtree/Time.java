package com.jvmops.gumtree;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public interface Time {
    default LocalDateTime now() {
        return LocalDateTime.now();
    }
}

@Component
class Now implements Time {}