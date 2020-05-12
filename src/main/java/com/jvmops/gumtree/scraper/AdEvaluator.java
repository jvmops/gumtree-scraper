package com.jvmops.gumtree.scraper;

import com.jvmops.gumtree.scraper.model.ScrappedAd;
import com.jvmops.gumtree.scraper.ports.ScrappedAdRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

@Component
@AllArgsConstructor
@Slf4j
public class AdEvaluator {
    static final BiPredicate<ScrappedAd, ScrappedAd> GUMTREE_ID_HAS_CHANGED =
            (saved, scrapped) -> ! Objects.equals(
                    scrapped.getGumtreeId(), saved.getGumtreeId());

    static final BiPredicate<ScrappedAd, ScrappedAd> PRICE_HAS_CHANGED =
            (saved, scrapped) -> ! Objects.equals(
                    saved.getPrice(), scrapped.getPrice());

    private ScrappedAdRepository scrappedAdRepository;

    @SuppressWarnings("squid:S3864")
    public ScrappedAd processAd(ScrappedAd scrapped) {
        ScrappedAd ad = findInRepository(scrapped)
                .map(saved -> modifyAd(saved, scrapped))
                .orElse(scrapped);
        return scrappedAdRepository.save(ad);
    }

    Optional<ScrappedAd> findInRepository(ScrappedAd scrapped) {
        Assert.hasText(scrapped.getCity(), "Scrapped ad does not contain city!");
        Assert.hasText(scrapped.getTitle(), "Scrapped ad does not contain title!");
        // this lookup is to figure out if the ad was re-posted by the same title
        return scrappedAdRepository.findByCityAndTitle(scrapped.getCity(), scrapped.getTitle());
    }

    // this is about the ads with the same title but different gumtreeId
    private ScrappedAd modifyAd(ScrappedAd saved, ScrappedAd scrapped) {
        if (GUMTREE_ID_HAS_CHANGED.test(saved, scrapped)) {
            log.debug("{} ad was reposted: \"{}\"", saved.getCity(), saved.getTitle());
            saved.setGumtreeId(scrapped.getGumtreeId());

            log.trace("Updating gumtree modtime from {} to {}", saved.getGumtreeModificationDate(), scrapped.getGumtreeModificationDate());
            saved.setGumtreeModificationDate(scrapped.getGumtreeCreationDate());

            log.trace("Updating url from: {} to {}", saved.getUrl(), scrapped.getUrl());
            saved.setUrl(scrapped.getUrl());

            if (PRICE_HAS_CHANGED.test(saved, scrapped)) {
                var priceChange = new PriceChange(
                        saved.getGumtreeModificationDate(),
                        saved.getPrice(),
                        scrapped.getPrice());
                log.trace("Price has changed from: {}, to: {}", priceChange.oldPrice(), priceChange.newPrice());
                saved.setPrice(priceChange.newPrice());
                // TODO: WIP
                // saved.addToPriceHistory(priceChange);
            }
        }
        return saved;
    }
}
