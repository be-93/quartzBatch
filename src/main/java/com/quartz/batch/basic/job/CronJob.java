package com.quartz.batch.basic.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CronJob implements Job {

	private volatile Thread currThread;

	@Autowired
	private BusinessJob businessJob;

	@SneakyThrows
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey = context.getJobDetail().getKey();
		currThread = Thread.currentThread();

		log.info("============================================================================");
		log.info(jobKey.getName() + " Job started :: jobName : {} - {}", jobKey.getName(), currThread.getName());
		businessJob.getClass().getMethod(jobKey.getName()).invoke(businessJob);
//		Method method = BusinessJob.class.newInstance().getClass().getMethod(jobKey.getName());
//		method.invoke(BusinessJob.class.newInstance());
		log.info(jobKey.getName() + " CronJob ended :: jobKey : {} - {}", jobKey, currThread.getName());
		log.info("============================================================================");



	}

}
