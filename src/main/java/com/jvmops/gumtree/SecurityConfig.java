package com.jvmops.gumtree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import javax.annotation.PostConstruct;

@Configuration
@Profile("web")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TemplateEngine templateEngine;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/subscribe").permitAll()
                    .antMatchers("/unsubscribe").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .anyRequest().authenticated()
                .and().formLogin()
                    .loginPage("/login")
                .defaultSuccessUrl("/cities")
                    .permitAll()
                .and().logout()
                    .logoutSuccessUrl("/");
    }

    @Profile("web")
    @Configuration
    class SecurityWebConfig implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/login").setViewName("login");
        }
    }

    // TODO: very very ugly hack - try think of something better
    // this is for the sec tag in html (xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4")
    @PostConstruct
    void addDialect() {
        templateEngine.addDialect(new SpringSecurityDialect());
    }
}
