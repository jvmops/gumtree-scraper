package com.jvmops.gumtree.scrapper;

import org.springframework.util.StringUtils;

// just for lulz and my own reference
public interface PriceParser {
    default Integer parse(String priceSpanValue) {
        if (StringUtils.hasText(priceSpanValue)
                && priceSpanValue.contains("zł")) {
            String price = priceSpanValue.replace("zł", "")
                    .trim()
                    .replace(" ", ""); // "2 200"
            return Integer.valueOf(price);
        } else {
            return 0;
        }
    }
}
