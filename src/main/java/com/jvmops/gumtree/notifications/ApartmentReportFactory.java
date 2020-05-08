package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.subscriptions.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jvmops.gumtree.notifications.CategoryType.*;

@Component
@Lazy
@Slf4j
class ApartmentReportFactory {
    private Map<CategoryType, CategoryFactory> categoryFactories;

    ApartmentReportFactory(List<CategoryFactory> categoryFactories) {
        this.categoryFactories = categoryFactories.stream()
                .collect(Collectors.toMap(
                        CategoryFactory::categoryType,
                        Function.identity()
                ));
    }

    ApartmentReport create(City city, ReportType reportType) {
        List<CategoryFactory> filteredCategoryFactories = switch(reportType) {
            case INITIAL -> List.of(
                    categoryFactories.get(NEWEST),
                    categoryFactories.get(DISHWASHER_AND_GAS),
                    categoryFactories.get(DISHWASHER_ONLY),
                    categoryFactories.get(CHEAPEST));
            case DAILY -> List.of(
                    categoryFactories.get(DISHWASHER_AND_GAS),
                    categoryFactories.get(DISHWASHER_ONLY),
                    categoryFactories.get(CHEAPEST));
            case NEWEST -> List.of(categoryFactories.get(NEWEST));
        };

        List<Category> categories = filteredCategoryFactories.stream()
                .map(categoryFactory -> categoryFactory.get(city.getName()))
                .collect(Collectors.toList());
        List<Category> emptyCategories = categories.stream()
                .filter(Category::isEmpty)
                .collect(Collectors.toList());

        if (categories.size() == emptyCategories.size()) {
            log.info("All categories from {} {} report are empty!", city.getName(), reportType);
            return ApartmentReport.empty();
        }

        String title = String.format(reportType.getTitleTemplate(), city.getName());

        return ApartmentReport.builder()
                .reportType(reportType)
                .city(city)
                .title(title)
                .categories(categories)
                .build();
    }
}
