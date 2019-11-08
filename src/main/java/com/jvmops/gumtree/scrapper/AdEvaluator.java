package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
class AdEvaluator {
    AdRepository adRepository;

    void processAds(List<Ad> ads) {
        ads.stream()
                .map(this::findInDb)
                .map(this::updateCreationDateIfPossible)
                .map(AdWrapper::getTheOneToSave)
                .forEach(adRepository::save);
    }

    Ad findInRepository(Ad scrapped) {
        return adRepository.findByTitle(scrapped.getTitle());
    }

    private AdWrapper process(Ad scrapped) {
        Ad fromDb = findInRepository(scrapped);
        return new AdWrapper(scrapped, fromDb);
    }

    private AdWrapper updateCreationDateIfPossible(AdWrapper adWrapper) {
        Ad fromDb = adWrapper.getFromDb();
        if (fromDb != null && creationDateIsDifferent(adWrapper)) {
            fromDb.setGumtreeCreationDate(adWrapper.getScrapped().getGumtreeCreationDate());
        }
        return adWrapper;
    }

    private boolean creationDateIsDifferent(AdWrapper adWrapper) {
        return ! Objects.equals(
                adWrapper.getFromDb().getGumtreeCreationDate(),
                adWrapper.getScrapped().getGumtreeCreationDate()
        );
    }
}

@Getter
@AllArgsConstructor
class AdWrapper {
    Ad scrapped;
    Ad fromDb;

    Ad getTheOneToSave() {
        return fromDb != null ? fromDb : scrapped;
    }
}
