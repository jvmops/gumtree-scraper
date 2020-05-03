package com.jvmops.gumtree.scrapper;

import org.springframework.stereotype.Component;

public interface Slowdown {
    void waitABit();
}

@Component
class SmallSlowdown implements Slowdown {
    @Override
    public void waitABit() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
