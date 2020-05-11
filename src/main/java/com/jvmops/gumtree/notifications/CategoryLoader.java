package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.Category;
import com.jvmops.gumtree.notifications.model.CategoryType;

public interface CategoryLoader {
    CategoryType categoryType();
    Category load(String city);
}
