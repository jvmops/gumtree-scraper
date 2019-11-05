package com.jvmops.gumtree.config.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Properties;

@Profile("scheduler")
@Configuration
@Slf4j
public class Quartz {
    @Bean
    SpringBeanJobFactory jobFactory(ApplicationContext applicationContext) {
        log.info("Initializing AutowiringSpringBeanJobFactory with application context");
        AutowiringJobFactory jobFactory = new AutowiringJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory)
            throws IOException {
        log.debug("Scheduler factory bean about to be initialized");
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setAutoStartup(true);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        log.info("Scheduler factory bean initialized");
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
