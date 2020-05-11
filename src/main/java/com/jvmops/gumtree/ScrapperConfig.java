package com.jvmops.gumtree;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
@Slf4j
public class ScrapperConfig {
    public static final CurrencyUnit DEFAULT_CURRENCY = CurrencyUnit.of("PLN");

    @Bean
    MongoCustomConversions customConverters() {
        log.debug("Registering custom money converters for mongodb");
        var moneyConverters = List.of(new MoneyWriter(), new MoneyReader());
        return new MongoCustomConversions(moneyConverters);
    }

    @WritingConverter
    class MoneyWriter implements Converter<Money, Integer> {
        @Override
        public Integer convert(Money source) {
            if (source == null) {
                throw new IllegalStateException("Unable to convert Money object to the amount. Money object is null.");
            }
            return source.getAmountMajorInt();
        }
    }

    @ReadingConverter
    class MoneyReader implements Converter<Integer, Money> {
        @Override
        public Money convert(Integer source) {
            if (source == null) {
                throw new IllegalStateException("Unable to convert amount to the Money object. Amount is null.");
            }
            return Money.of(DEFAULT_CURRENCY, source);
        }
    }
}
