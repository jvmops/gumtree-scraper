package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.notifications.model.ApartmentReport;
import com.jvmops.gumtree.notifications.model.ApartmentReportType;
import com.jvmops.gumtree.notifications.model.Category;
import com.jvmops.gumtree.notifications.model.CategoryType;
import com.jvmops.gumtree.subscriptions.model.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jvmops.gumtree.notifications.model.ApartmentReportType.INITIAL;
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
        List<CategoryLoader> categoryLoaders = switch(apartmentReportType) {
            case INITIAL -> List.of(
                    this.categoryLoaders.get(NEWS),
                    this.categoryLoaders.get(DISHWASHER_AND_GAS),
                    this.categoryLoaders.get(DISHWASHER_ONLY),
                    this.categoryLoaders.get(CHEAPEST));
            case DAILY -> List.of(
                    this.categoryLoaders.get(DISHWASHER_AND_GAS),
                    this.categoryLoaders.get(DISHWASHER_ONLY),
                    this.categoryLoaders.get(CHEAPEST));
            case NEWEST -> List.of(this.categoryLoaders.get(NEWS));
        };

        List<Category> loadedCategories = categoryLoaders.stream()
                .map(categoryLoader -> categoryLoader.load(city.getName()))
                .collect(Collectors.toList());
        List<Category> emptyCategories = loadedCategories.stream()
                .filter(Category::isEmpty)
                .collect(Collectors.toList());

        if (loadedCategories.size() == emptyCategories.size()) {
            log.warn("{} apartment report from {} is empty", INITIAL, city.getName());
            return ApartmentReport.empty(city, apartmentReportType);
        }

        return ApartmentReport.builder()
                .apartmentReportType(apartmentReportType)
                .city(city)
                .categories(loadedCategories)
                .build();
    }
}
