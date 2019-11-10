package com.jvmops.gumtree.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public interface Time {
    default LocalDateTime now() {
        return LocalDateTime.now();
    }
}

@Component
@ConditionalOnMissingBean(Time.class)
class Now implements Time {}