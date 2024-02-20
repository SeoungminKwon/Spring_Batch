package com.ll.sbbatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BatchService {

    //스프링 배치 작업을 실행하는 인터페이스, Job실행, 필요한 매개변수를 전달 할 수 있다.
    private final JobLauncher jobLauncher;
    private final Job helloJob;
    private final Job makeProductLogJob;

    public void runHelloJobJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .toJobParameters();
            jobLauncher.run(helloJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runMakeProductLogJob(LocalDateTime startDate_, LocalDateTime endDate_) {

        try{
            String startDate = startDate_.toString().substring(0, 10) + " 00:00:00.000000";
            String endDate = endDate_.toString().substring(0, 10) + " 23:59:59.999999";

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("startDate", startDate)
                    .addString("endDate", endDate)
                    .toJobParameters();
            jobLauncher.run(makeProductLogJob, jobParameters);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
