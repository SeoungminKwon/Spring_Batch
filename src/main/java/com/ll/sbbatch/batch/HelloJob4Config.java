package com.ll.sbbatch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class HelloJob4Config {
    @Bean
    public Job hello4Job(JobRepository jobRepository, Step hello4Step1) {
        return new JobBuilder("hello4Job", jobRepository)
                .start(hello4Step1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    //Chunk방식으로 Step Build
    @JobScope
    @Bean
    public Step hello4Step1(
            JobRepository jobRepository,
            Hello4Step1Reader hello4Step1Reader,
            Hello4Step1Processor hello4Step1Processor,
            Hello4Step1Writer hello4Step1Writer,
            PlatformTransactionManager platformTransactionManager
    ){
        return new StepBuilder("hello4Step1Tasklet", jobRepository)
                .<Integer, String> chunk(10, platformTransactionManager)
                .reader(hello4Step1Reader)
                .processor(hello4Step1Processor)
                .writer(hello4Step1Writer)
                .build();
    }

    //ChunkReader
    @StepScope
    @Component
    public static class Hello4Step1Reader implements ItemReader<Integer> {
        @Override
        public Integer read() {
            int no = (int) (Math.random() * 500);

            if(no == 100) return null;
            return no;
        }
    }

    //ChunkProcessor
    //원본 데이터 가공해서 파생 데이터 생성
    //Ex. 50 -> "no. 50"
    @StepScope
    @Component
    public static class Hello4Step1Processor implements ItemProcessor<Integer, String> {
        @Override
        public String process(Integer item) {
            return "no." + item;
        }
    }

    //파생 데이터를 화면에 출력
    @StepScope
    @Component
    public static class Hello4Step1Writer implements ItemWriter<String>{
        @Override
        public void write(Chunk< ? extends String > chunk) {
            List< String > items = (List< String >) chunk.getItems();
            for (String item : items) {
                System.out.println("item = " + item);
            }
        }
    }
}
