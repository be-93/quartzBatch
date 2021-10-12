package com.quartz.batch.basic.service.impl;

import com.quartz.batch.basic.dto.scheduler.JobRequest;
import com.quartz.batch.basic.dto.scheduler.JobResponse;
import com.quartz.batch.basic.service.ScheduleService;
import com.quartz.batch.utils.DateTimeUtils;
import com.quartz.batch.utils.JobUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ApplicationContext context;

    @Override
    public boolean addJob(JobRequest jobRequest, Class<? extends Job> jobClass) {
        JobKey jobKey = null;
        JobDetail jobDetail;
        Trigger trigger;

        try {
            trigger = JobUtils.createTrigger(jobRequest);
            jobDetail = JobUtils.createJob(jobRequest, jobClass, context);
            jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());


            Date dt = schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
            log.debug("Job with jobKey : {} scheduled successfully at date : {}", jobDetail.getKey(), dt);
            return true;
        } catch (SchedulerException e) {
            log.error("error occurred while scheduling with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean deleteJob(JobKey jobKey) {
        log.debug("[schedulerdebug] deleting job with jobKey : {}", jobKey);
        try {
            return schedulerFactoryBean.getScheduler().deleteJob(jobKey);
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while deleting job with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean updateJob(JobRequest jobRequest) {
        JobKey jobKey = null;
        Trigger newTrigger;

        try {
            newTrigger = JobUtils.createTrigger(jobRequest);
            jobKey = JobKey.jobKey(jobRequest.getJobName(), jobRequest.getJobGroup());

            Date dt = schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobRequest.getJobName()), newTrigger);
            log.debug("Job with jobKey : {} rescheduled successfully at date : {}", jobKey, dt);
            return true;
        } catch (SchedulerException e) {
            log.error("error occurred while scheduling with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean pauseJob(JobKey jobKey) {
        log.debug("[schedulerdebug] pausing job with jobKey : {}", jobKey);
        try {
            schedulerFactoryBean.getScheduler().pauseJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while deleting job with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean resumeJob(JobKey jobKey) {
        log.debug("[schedulerdebug] resuming job with jobKey : {}", jobKey);
        try {
            schedulerFactoryBean.getScheduler().resumeJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while resuming job with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean stopJob(JobKey jobKey) {
        log.debug("[schedulerdebug] stopping job with jobKey : {}", jobKey);
        try {
            return schedulerFactoryBean.getScheduler().interrupt(jobKey);
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while stopping job with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public List<JobResponse> findAllJobs() {

        List<JobResponse> jobs = new ArrayList<>();

        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            for (String groupName : scheduler.getJobGroupNames()) {

                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                    List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);

                    CronTrigger cronTrigger = (CronTrigger)triggers.get(0);
                    JobResponse jobResponse = JobResponse.builder()
                            .jobName(jobKey.getName())
                            .groupName(jobKey.getGroup())
                            .scheduleTime(DateTimeUtils.toString(triggers.get(0).getStartTime()))
                            .lastFiredTime(DateTimeUtils.toString(triggers.get(0).getPreviousFireTime()))
                            .nextFireTime(DateTimeUtils.toString(triggers.get(0).getNextFireTime()))
                            .cronExpression(cronTrigger.getCronExpression())
                            .build();

                    jobs.add(jobResponse);
                }
            }
        } catch (SchedulerException e) {
            log.error("[error] findAllJobs : Job 전체 조회중 오류 ", e);
        }

        return jobs;
    }

    @Override
    public boolean isJobRunning(JobKey jobKey) {
        try {
            List<JobExecutionContext> currentJobs = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
            if (currentJobs != null) {
                for (JobExecutionContext jobCtx : currentJobs) {
                    if (jobKey.getName().equals(jobCtx.getJobDetail().getKey().getName())) {
                        return true;
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job with jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public boolean isJobExists(JobKey jobKey) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            if (scheduler.checkExists(jobKey)) {
                return true;
            }
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] error occurred while checking job exists :: jobKey : {}", jobKey, e);
        }
        return false;
    }

    @Override
    public String getJobState(JobKey jobKey) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);

            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());

            if (triggers != null && triggers.size() > 0) {
                for (Trigger trigger : triggers) {
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    if (Trigger.TriggerState.NORMAL.equals(triggerState)) {
                        return "SCHEDULED";
                    }
                    return triggerState.name().toUpperCase();
                }
            }
        } catch (SchedulerException e) {
            log.error("[schedulerdebug] Error occurred while getting job state with jobKey : {}", jobKey, e);
        }
        return null;
    }
}
