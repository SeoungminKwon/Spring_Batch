package com.ll.sbbatch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
public class HelloJobConfig {
    @Bean
    public Job helloJob(JobRepository jobRepository, Step helloStep1){
        return new JobBuilder("helloJob", jobRepository)
                .start(helloStep1)
                /**JobParametersIncrementer인터페이스 구현체 RunIdIncrementer은 배치 작업 실행시마다 JobParameters에 고유한
                 * 실행 Id를 추가 하여 같은 배치 작업이라도 서로 다른 실행으로 구분 될 수 있게 한다.
                 */
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @JobScope
    @Bean
    public Step helloStep1(JobRepository jobRepository, Tasklet helloStep1Tasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("helloStep1Tasklet", jobRepository)
                .tasklet(helloStep1Tasklet, platformTransactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloStep1Tasklet() {
        return((contribution, chunkContext) -> {
            log.info("Hello World");
            System.out.println("Hello World 1-1");
            return RepeatStatus.FINISHED;
        });
    }
}
