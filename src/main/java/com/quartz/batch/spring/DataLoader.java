package com.quartz.batch.spring;

import com.quartz.batch.basic.dto.scheduler.JobRequest;
import com.quartz.batch.basic.job.CronJob;
import com.quartz.batch.basic.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ScheduleService scheduleService;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {



    }

}
