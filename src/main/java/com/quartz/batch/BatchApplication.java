package com.quartz.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BatchApplication extends SpringBootServletInitializer {

	// 외부 tomcat 환경시 주석해제 필요
//	@Override protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(BatchApplication.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

}
