package com.jvmops.gumtree;

import com.jvmops.gumtree.scrapper.Slowdown;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class NoSlowdown implements Slowdown {
    @Override
    public void waitABit() {}
}
