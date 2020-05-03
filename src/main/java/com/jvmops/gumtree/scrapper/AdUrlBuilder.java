package com.jvmops.gumtree.scrapper;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.jvmops.gumtree.scrapper.HtmlProvider.GUMTREE_URL;

@Component
class AdUrlBuilder {
    AdUrl buildAdUrl(String urlSuffix) {
        String[] urlParts = urlSuffix.split("/");
        String gumtreeId = urlParts[urlParts.length-1];
        StringBuilder urlBuilder = new StringBuilder(GUMTREE_URL);
        for (int i = 0; i < urlParts.length -2; i++) {
            if (StringUtils.isEmpty(urlParts[i])) {
                continue;
            }
            urlBuilder.append("/" + urlParts[i]);
        }
        urlBuilder.append("/" + gumtreeId);
        return new AdUrl(gumtreeId, urlBuilder.toString());
    }

    record AdUrl(String gumtreeId, String url) {}
}
