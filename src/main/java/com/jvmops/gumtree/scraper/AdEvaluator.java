package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.ports.ScrappedAdRepository;
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
public class AdEvaluator {
    private ScrappedAdRepository scrappedAdRepository;

    @SuppressWarnings("squid:S3864")
    public void processAd(ScrappedAd scrapped) {
        ScrappedAd ad = findInRepository(scrapped)
                .map(saved -> updateGumtreeModificationTime(saved, scrapped))
                .orElse(scrapped);
        // TODO: check if price changed?

        logIfNew(ad);
        scrappedAdRepository.save(ad);
    }

    Optional<ScrappedAd> findInRepository(ScrappedAd scrapped) {
        Assert.hasText(scrapped.getCity(), "Scrapped ad does not contain city!");
        Assert.hasText(scrapped.getTitle(), "Scrapped ad does not contain title!");
        // this lookup is to figure out if the ad was re-posted by the same title
        return scrappedAdRepository.findByCityAndTitle(scrapped.getCity(), scrapped.getTitle());
    }

    // this is about the ads with the same title but different gumtreeId
    private ScrappedAd updateGumtreeModificationTime(ScrappedAd saved, ScrappedAd scrapped) {
        if (scrappedIsNewer(saved, scrapped)) {
            log.info("Updating gumtree creation time for \"{}\" :: {}", saved.getTitle(), saved.getId());
            saved.setGumtreeModificationDate(scrapped.getGumtreeCreationDate());
        }
        return saved;
    }

    private boolean scrappedIsNewer(ScrappedAd saved, ScrappedAd scrapped) {
        return ! Objects.equals(
                scrapped.getGumtreeCreationDate(),
                saved.getGumtreeCreationDate()
        );
    }

    private void logIfNew(ScrappedAd ad) {
        if (isNull(ad.getId())) {
            log.debug("Saving \"{}\"", ad.getTitle());
        }
    }
}