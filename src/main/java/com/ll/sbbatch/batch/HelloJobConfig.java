package com.ll.sbbatch.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
    public Job helloJob(JobRepository jobRepository, Step simpleStep1){
        return new JobBuilder("helloJob", jobRepository)
                .start(simpleStep1)
                /**JobParametersIncrementer인터페이스 구현체 RunIdIncrementer은 배치 작업 실행시마다 JobParameters에 고유한
                 * 실행 Id를 추가 하여 같은 배치 작업이라도 서로 다른 실행으로 구분 될 수 있게 한다.
                 */
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step helloStep1(JobRepository jobRepository, Tasklet helloStep1Tasklet1, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("helloStep1Tasklet1", jobRepository)
                .tasklet(helloStep1Tasklet1, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet hellStep1Tasklet1() {
        return((contribution, chunkContext) -> {
            log.info("Hello World");
            System.out.println("Hello World");
            return RepeatStatus.FINISHED;
        });
    }
}
