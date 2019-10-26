package com.jvmops.gumtree.scrapper.ads;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
class AdPreProcessor {
    AdSummary parseAdSummary(WebElement webElement) {
        WebElement titleElement = webElement.findElement(By.className("title"));
        String url = titleElement.findElement(By.tagName("a")).getAttribute("href");
        String price = webElement.findElement(By.className("ad-price"))
                .getText()
                .replace("zł", "")
                .trim()
                .replace(" ", "");
        return AdSummary.builder()
                .url(url)
                .title(titleElement.getText())
                .price(Integer.valueOf(price))
                .build();
    }
}
