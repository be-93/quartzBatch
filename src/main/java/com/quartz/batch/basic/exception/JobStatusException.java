package com.quartz.batch.basic.exception;

public class JobStatusException extends RuntimeException{

    public JobStatusException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

}
