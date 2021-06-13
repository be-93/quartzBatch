package com.quartz.batch.basic.service;

import com.quartz.batch.basic.dto.scheduler.JobRequest;
import com.quartz.batch.basic.dto.scheduler.StatusResponse;
import org.quartz.Job;
import org.quartz.JobKey;

public interface ScheduleService {
    StatusResponse getAllJobs();

    boolean isJobRunning(JobKey jobKey);

    boolean isJobExists(JobKey jobKey);

    boolean addJob(JobRequest jobRequest, Class<? extends Job> jobClass);

    boolean deleteJob(JobKey jobKey);

    boolean pauseJob(JobKey jobKey);

    boolean resumeJob(JobKey jobKey);

    String getJobState(JobKey jobKey);

    boolean stopJob(JobKey jobKey);

    boolean updateJob(JobRequest jobRequest);
}
