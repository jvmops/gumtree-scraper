package com.jvmops.gumtree.config.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Slf4j
public final class AutowiringJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        log.info("Job {} initialized", bundle.getJobDetail().getJobClass().getSimpleName());
        beanFactory.autowireBean(job);
        return job;
    }
}
