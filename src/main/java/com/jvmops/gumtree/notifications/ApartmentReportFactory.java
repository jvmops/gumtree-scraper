package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.ApartmentReport;
import com.jvmops.gumtree.notifications.model.ApartmentReportType;
import com.jvmops.gumtree.notifications.model.Category;
import com.jvmops.gumtree.notifications.model.CategoryType;
import com.jvmops.gumtree.subscriptions.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jvmops.gumtree.notifications.model.CategoryType.*;

@Component
@Lazy
@Slf4j
public class ApartmentReportFactory {
    private Map<CategoryType, CategoryLoader> categoryLoaders;

    ApartmentReportFactory(List<CategoryLoader> categoryLoaders) {
        this.categoryLoaders = categoryLoaders.stream()
                .collect(Collectors.toMap(
                        CategoryLoader::categoryType,
                        Function.identity()
                ));
    }

    public ApartmentReport create(City city, ApartmentReportType apartmentReportType) {
        log.info("Loading {} apartment report for {}", city.getName(), apartmentReportType);
        var filteredCategories = switch(apartmentReportType) {
            case INITIAL -> List.of(
                    categoryLoaders.get(NEWS),
                    categoryLoaders.get(DISHWASHER_AND_GAS),
                    categoryLoaders.get(DISHWASHER_ONLY),
                    categoryLoaders.get(CHEAPEST));
            case DAILY -> List.of(
                    categoryLoaders.get(DISHWASHER_AND_GAS),
                    categoryLoaders.get(DISHWASHER_ONLY),
                    categoryLoaders.get(CHEAPEST));
            case NEWEST -> List.of(categoryLoaders.get(NEWS));
        };

        List<Category> loadedCategories = filteredCategories.stream()
                .map(categoryLoader -> categoryLoader.load(city.getName()))
                .collect(Collectors.toList());
        List<Category> emptyCategories = loadedCategories.stream()
                .filter(Category::isEmpty)
                .collect(Collectors.toList());

        if (loadedCategories.size() == emptyCategories.size()) {
            log.warn("{} {} apartment report is empty", city.getName(), ApartmentReportType.INITIAL);
            return ApartmentReport.empty(city, apartmentReportType);
        }

        return ApartmentReport.builder()
                .apartmentReportType(apartmentReportType)
                .city(city)
                .categories(loadedCategories)
                .build();
    }
}
