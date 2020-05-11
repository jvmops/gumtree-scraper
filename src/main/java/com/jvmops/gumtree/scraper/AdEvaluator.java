package com.jvmops.gumtree.scraper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
@Slf4j
class AdEvaluator {
    private ScrappedAdRepository scrappedAdRepository;

    @SuppressWarnings("squid:S3864")
    void processAd(Ad scrapped) {
        Ad ad = findInRepository(scrapped)
                .map(saved -> updateModificationTime(saved, scrapped))
                .orElse(scrapped);
        // TODO: check if price changed?

        logIfNew(ad);
        scrappedAdRepository.save(ad);
    }

    Optional<Ad> findInRepository(Ad scrapped) {
        Assert.hasText(scrapped.getCity(), "Scrapped ad does not contain city!");
        Assert.hasText(scrapped.getTitle(), "Scrapped ad does not contain title!");
        // this lookup is to figure out if the ad was re-posted by the same title
        return scrappedAdRepository.findByCityAndTitle(scrapped.getCity(), scrapped.getTitle());
    }

    // TODO: update the list and do not change the gumtree creation time
    private Ad updateModificationTime(Ad saved, Ad scrapped) {
        if (scrappedIsNewer(saved, scrapped)) {
            log.info("Updating gumtree creation time for \"{}\" :: {}", saved.getTitle(), saved.getId());
            saved.setGumtreeCreationDate(scrapped.getGumtreeCreationDate());
        }
        return saved;
    }

    private boolean scrappedIsNewer(Ad saved, Ad scrapped) {
        return ! Objects.equals(
                scrapped.getGumtreeCreationDate(),
                saved.getGumtreeCreationDate()
        );
    }

    private void logIfNew(Ad ad) {
        if (isNull(ad.getId())) {
            log.debug("Saving \"{}\"", ad.getTitle());
        }
    }
}