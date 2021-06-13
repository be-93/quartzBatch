package com.quartz.batch.basic.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class BusinessJob {


    public void actionLogic1() {
        System.out.println(" method - actionLogic1");

    }
    public void actionLogic2() {
        System.out.println(" method - actionLogic2");

    }


}
