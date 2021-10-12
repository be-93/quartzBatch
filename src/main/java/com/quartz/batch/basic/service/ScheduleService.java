package com.quartz.batch.basic.service;

import com.quartz.batch.basic.dto.scheduler.JobRequest;
import com.quartz.batch.basic.dto.scheduler.JobResponse;
import org.quartz.Job;
import org.quartz.JobKey;

import java.util.List;

public interface ScheduleService {
    List<JobResponse> findAllJobs();

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
