package com.ll.sbbatch.batch;

import com.ll.sbbatch.domain.product.product.entity.Product;
import com.ll.sbbatch.domain.product.product.entity.ProductLog;
import com.ll.sbbatch.domain.product.product.repository.ProductLogRepository;
import com.ll.sbbatch.domain.product.product.repository.ProductRepository;
import com.ll.sbbatch.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class MakeProductLogJobConfig {
    private final int CHUNK_SIZE = 20;
    private final ProductRepository productRepository;
    private final ProductLogRepository productLogRepository;

    @Bean
    public Job makeProductLogJob(JobRepository jobRepository, Step makeProductLogStep1){
        return new JobBuilder("makeProductLogJob", jobRepository)
                .start(makeProductLogStep1)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @JobScope
    @Bean
    public Step makeProductLogStep1(
            JobRepository jobRepository,
            ItemReader< Product > step1Reader,
            ItemProcessor<Product, ProductLog > step1Processor,
            ItemWriter<ProductLog> step1Writer,
            PlatformTransactionManager platformTransactionManager
    ){
        return new StepBuilder("makeProductLogStep1Tasklet", jobRepository)
                .<Product, ProductLog>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(step1Reader)
                .processor(step1Processor)
                .writer(step1Writer)
                .build();
    }

    @StepScope
    @Bean
    public ItemReader<Product> step1Reader(
            @Value("#{jobParameters['startDate']}") String _startDate,
            @Value("#{jobParameters['endDate']}") String _endDate
            //@Value는 스프링 의존성 주입 기능 중 하나로 프로퍼티 파일, 환경 변수, 정의된 변수 ... 당양한 소스로 부터 값을 주입받는데 사용
    ){

        LocalDateTime startDate = Ut.date.parse(_startDate);
        LocalDateTime endDate = Ut.date.parse(_endDate);

        return new RepositoryItemReaderBuilder<Product>()
                .name("step1Reader") // 빈 이름 설정
                .repository(productRepository) //데이터를 읽어올 저장소
                .methodName("findByCreateDateBetween") //저장소에서 호출할 메서드 이름
                .pageSize(CHUNK_SIZE) //읽기 작업 페이지 크기 지정
                .arguments(Arrays.asList(startDate, endDate)) //findByCreateDateBetween에 넘겨줄 인자
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC)) //데이터를 읽어올시 정렬 방식
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Product, ProductLog> step1Processor(){
        return product -> ProductLog
                .builder()
                .product(product)
                .name(product.getName())
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<ProductLog> step1Writer(){
        return items -> items.forEach(item -> {
            productLogRepository.save(item);
        });
    }
}
