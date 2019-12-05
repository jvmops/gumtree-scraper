package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
@Slf4j
class AdEvaluator {
    AdScrapperRepository adScrapperRepository;

    void processAds(List<Ad> ads) {
        ads.stream()
                .map(this::findAndWrap)
                .map(AdWrapper::updateCreationDateIfPossible)
                .peek(this::logIfNew)
                .forEach(adScrapperRepository::save);
    }

    private void logIfNew(Ad ad) {
        if (isNull(ad.getId())) {
            log.info("Saving \"{}\"", ad.getTitle());
        }
    }

    private AdWrapper findAndWrap(Ad scrapped) {
        return new AdWrapper(scrapped, findInRepository(scrapped));
    }

    Optional<Ad> findInRepository(Ad scrapped) {
        Assert.hasText(scrapped.getCity(), "Scrapped ad does not contain city!");
        Assert.hasText(scrapped.getTitle(), "Scrapped ad does not contain title!");
        return adScrapperRepository.findByCityAndTitle(scrapped.getCity(), scrapped.getTitle());
    }
}

@Getter
@Slf4j
@AllArgsConstructor
class AdWrapper {
    private Ad scrapped;
    private Optional<Ad> fromDbOpt;

    Ad updateCreationDateIfPossible() {
        if (fromDbOpt.isPresent() && scrappedIsNewer()) {
            Ad fromDb = fromDbOpt.get();
            log.info("Updating gumtree creation time for \"{}\" :: {}", fromDb.getTitle(), fromDb.getId());
            fromDb.setGumtreeCreationDate(scrapped.getGumtreeCreationDate());
        }
        return getTheOneToSave();
    }

    private Ad getTheOneToSave() {
        return fromDbOpt.orElse(scrapped);
    }

    private boolean scrappedIsNewer() {
        return ! Objects.equals(
                fromDbOpt.get().getGumtreeCreationDate(),
                scrapped.getGumtreeCreationDate()
        );
    }
}
