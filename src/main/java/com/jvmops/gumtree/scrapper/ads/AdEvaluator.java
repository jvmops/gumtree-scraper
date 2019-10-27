package com.jvmops.gumtree.scrapper.ads;

import org.springframework.stereotype.Component;

@Component
class AdEvaluator {
    AdRepository adRepository;

    boolean isNew(Ad ad) {
        return false;
    }
}
