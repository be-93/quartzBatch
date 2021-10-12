package com.quartz.batch.basic.dto.scheduler;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class ApiResponse {

	private String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	private String message; // 예외 메세지
	private int status; // HTTP 상태값
	private Boolean success;

	public ApiResponse(Boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	static public ApiResponse create() {
		return new ApiResponse(true , "");
	}

	public ApiResponse status(int status) {
		this.status = status;
		return this;
	}

	public ApiResponse message(String message) {
		this.message = message;
		return this;
	}

	public ApiResponse success(boolean success) {
		this.success = success;
		return this;
	}

}
