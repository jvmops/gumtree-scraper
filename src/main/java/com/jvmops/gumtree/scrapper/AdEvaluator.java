package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
class AdEvaluator {
    AdRepository adRepository;

    void processAds(Stream<Ad> ads) {
        ads.map(this::findInDb)
                .map(this::updateCreationDateIfPossible)
                .map(AdWrapper::getTheOneToSave)
                .forEach(adRepository::save);
    }

    private AdWrapper findInDb(Ad scrapped) {
        Ad fromDb = adRepository.findByTitle(scrapped.getTitle());
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

    @Getter
    @AllArgsConstructor
    private static class AdWrapper {
        Ad scrapped;
        Ad fromDb;

        Ad getTheOneToSave() {
            return fromDb != null ? fromDb : scrapped;
        }
    }
}
