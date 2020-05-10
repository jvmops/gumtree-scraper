package com.jvmops.gumtree.notifications;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
class Category {
    private CategoryType type;
    private List<Ad> ads;

    public String getHeader() {
        return type.getHeader();
    }

    boolean isEmpty() {
        return ads.isEmpty();
    }
}
