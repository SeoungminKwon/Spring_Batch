package com.ll.sbbatch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchTestConfig {

    //스프링 배치 테스트를 위해서는 JobLauncherTestUtils가 빈으로 등록되어야함
    @Bean
    public JobLauncherTestUtils helloJobLauncherTestUtils(Job helloJob){
        JobLauncherTestUtils utils = new JobLauncherTestUtils();
        utils.setJob(helloJob);
        return utils;
    }

    @Bean
    public JobLauncherTestUtils hello2JobLauncherTestUtils(Job hello2Job){
        JobLauncherTestUtils utils = new JobLauncherTestUtils();
        utils.setJob(hello2Job);
        return utils;
    }
}
