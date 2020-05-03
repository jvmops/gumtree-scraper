package com.jvmops.gumtree.scrapper;

import com.jvmops.gumtree.Main;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Main.class)
public class ScrapperTest {

    private Scrapper scrapper;

    @Autowired
    private ListedAdRepository listedAdRepository;

    @Mock
    private HtmlProvider htmlProvider;

    @BeforeEach
    public void setup() {
        JSoupAdListingScrapper adListingScrapper = new JSoupAdListingScrapper(htmlProvider);
        JSoupAdDetailsScrapper adDetailsScrapper = new JSoupAdDetailsScrapper(htmlProvider);
        scrapper = new Scrapper(adListingScrapper, adDetailsScrapper, listedAdRepository);
    }
}
