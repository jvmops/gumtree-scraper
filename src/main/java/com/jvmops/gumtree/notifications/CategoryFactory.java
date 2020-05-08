package com.jvmops.gumtree.notifications;

public interface CategoryFactory {
    CategoryType categoryType();
    Category get(String city);
}
