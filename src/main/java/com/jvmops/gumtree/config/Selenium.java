package com.jvmops.gumtree.config;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class Selenium {
    @Value("${gumtree.scrapper.selenium-url}")
    private String seleniumUrl;

    @Bean
    WebDriverFactory webDriverFactory() {
        return new WebDriverFactory(seleniumUrl);
    }

    public class WebDriverFactory {
        private URL seleniumServerUrl;

        WebDriverFactory(String seleniumServerUrl) {
            try {
                this.seleniumServerUrl = new URL(seleniumServerUrl);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public RemoteWebDriver initialize() {
            DesiredCapabilities chrome = DesiredCapabilities.chrome();
            return new RemoteWebDriver(seleniumServerUrl, chrome);
        }
    }
}
