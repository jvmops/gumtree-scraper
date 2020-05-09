package com.jvmops.gumtree.notifications;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
class Category {
    private CategoryType type;
    private String header;
    private List<Ad> ads;

    boolean isEmpty() {
        return ads.isEmpty();
    }
}
