package com.quartz.batch.basic.controller;

import com.quartz.batch.basic.dto.scheduler.ApiResponse;
import com.quartz.batch.basic.dto.scheduler.JobRequest;
import com.quartz.batch.basic.dto.scheduler.JobResponse;
import com.quartz.batch.basic.job.CronJob;
import com.quartz.batch.basic.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/scheduler")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping(value = "/addJob", method = RequestMethod.POST)
    public ResponseEntity<?> addScheduleJob(@ModelAttribute JobRequest jobRequest) {

        scheduleService.addJob(jobRequest, CronJob.class);

        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        if (!scheduleService.isJobExists(jobKey)) {
            if (jobRequest.isJobTypeSimple()) {
                return new ResponseEntity<>(new ApiResponse(false, "not found cron"),
                        HttpStatus.BAD_REQUEST);
            } else {

            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Job already exits"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "Job created successfully"), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/deleteJob", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteScheduleJob(@RequestBody JobRequest jobRequest) {
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        if (scheduleService.isJobExists(jobKey)) {
            if (!scheduleService.isJobRunning(jobKey)) {
                scheduleService.deleteJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 실행 중입니다."), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "삭제 성공"), HttpStatus.OK);
    }

    @RequestMapping(value = "/updateJob", method = RequestMethod.PUT)
    public ResponseEntity<?> updateScheduleJob(@RequestBody JobRequest jobRequest) {
        log.debug("update schedule job :: jobRequest : {}", jobRequest);
        if (jobRequest.getJobName() == null) {
            return new ResponseEntity<>(new ApiResponse(false, "Require jobName"),
                    HttpStatus.BAD_REQUEST);
        }

        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        if (scheduleService.isJobExists(jobKey)) {
            scheduleService.updateJob(jobRequest);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 존재하지 않습니다."),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "수정 성공"), HttpStatus.OK);
    }

    @RequestMapping(value = "/jobs", method = RequestMethod.GET)
    public List<JobResponse> findAllJobs() {
        return scheduleService.findAllJobs();
    }

    @RequestMapping(value = "/pauseJob", method = RequestMethod.PUT)
    public ResponseEntity<?> pauseJob(@RequestBody JobRequest jobRequest) {
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        if (scheduleService.isJobExists(jobKey)) {
            if (!scheduleService.isJobRunning(jobKey)) {
                scheduleService.pauseJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, " JOB 은 현재 실행중이지 않습니다."), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "정지 성공"), HttpStatus.OK);
    }

    @RequestMapping(value = "/resumeJob", method = RequestMethod.PUT)
    public ResponseEntity<?> resumeJob(@RequestBody JobRequest jobRequest) {
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        if (scheduleService.isJobExists(jobKey)) {
            String jobState = scheduleService.getJobState(jobKey);

            if (jobState.equals("PAUSED")) {
                scheduleService.resumeJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 현재 실행중입니다."), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "재개 성공"), HttpStatus.OK);
    }

    @RequestMapping(value = "/stopJob", method = RequestMethod.PUT)
    public ResponseEntity<?> stopJob(@RequestBody JobRequest jobRequest) {
        JobKey jobKey = new JobKey(jobRequest.getJobName(), jobRequest.getJobGroup());
        if (scheduleService.isJobExists(jobKey)) {
            if (scheduleService.isJobRunning(jobKey)) {
                scheduleService.stopJob(jobKey);
            } else {
                return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 현재 실행중이지 않습니다."), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ApiResponse(false, jobKey.getName() + " JOB 은 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse(true, "정지 성공"), HttpStatus.OK);
    }



}
