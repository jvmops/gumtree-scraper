package com.jvmops.gumtree.security;

import com.jvmops.gumtree.user.JvmopsUserRepository;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    private JvmopsUserRepository jvmopsUserRepository;

//    @Override
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        return new JvmopsUserDetailsService(jvmopsUserRepository);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .anyRequest().authenticated()
                .and().formLogin()
                    .loginPage("/login")
                    .permitAll();
    }

    // TODO: very very ugly hack - try think of something better
    @PostConstruct
    void addDialect() {
        templateEngine.addDialect(new SpringSecurityDialect());
    }

    @Profile("web")
    @Configuration
    class SecurityWebConfig implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/login").setViewName("login");
        }
    }
}
