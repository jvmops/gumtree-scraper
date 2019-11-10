package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
class AdEvaluator {
    AdRepository adRepository;

    void processAds(List<Ad> ads) {
        ads.stream()
                .map(this::findAndWrap)
                .map(AdWrapper::updateCreationDateIfPossible)
                .forEach(adRepository::save);
    }

    private AdWrapper findAndWrap(Ad scrapped) {
        return new AdWrapper(scrapped, findInRepository(scrapped));
    }

    Optional<Ad> findInRepository(Ad scrapped) {
        return adRepository.findByTitle(scrapped.getTitle());
    }
}

@Getter
@Slf4j
@AllArgsConstructor
class AdWrapper {
    private Ad scrapped;
    private Optional<Ad> fromDb;

    Ad updateCreationDateIfPossible() {
        if (fromDb.isPresent() && scrappedIsNewer()) {
            log.info("updating gumtree creation time");
            fromDb.get().setGumtreeCreationDate(scrapped.getGumtreeCreationDate());
        }
        return getTheOneToSave();
    }

    private Ad getTheOneToSave() {
        return fromDb.orElse(scrapped);
    }

    private boolean scrappedIsNewer() {
        return ! Objects.equals(
                fromDb.get().getGumtreeCreationDate(),
                scrapped.getGumtreeCreationDate()
        );
    }
}
