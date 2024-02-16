package com.ll.sbbatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchService {

    //스프링 배치 작업을 실행하는 인터페이스, Job실행, 필요한 매개변수를 전달 할 수 있다.
    private final JobLauncher jobLauncher;
    private final Job helloJob;

    public void runHelloJobJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .toJobParameters();
            jobLauncher.run(helloJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
