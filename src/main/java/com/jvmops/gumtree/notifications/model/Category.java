package com.jvmops.gumtree.notifications.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Category {
    private CategoryType type;
    private List<Ad> ads;

    public String getHeader() {
        return type.getHeader();
    }

    public boolean isEmpty() {
        return ads.isEmpty();
    }
}
