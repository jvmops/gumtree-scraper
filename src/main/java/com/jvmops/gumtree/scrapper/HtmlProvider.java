package com.jvmops.gumtree.scrapper;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class HtmlProvider {
    String get(String url) {
        try {
            return Jsoup.connect(url).get().html();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}