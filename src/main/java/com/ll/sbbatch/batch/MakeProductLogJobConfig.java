package com.ll.sbbatch.batch;

import com.ll.sbbatch.domain.product.product.entity.Product;
import com.ll.sbbatch.domain.product.product.entity.ProductLog;
import com.ll.sbbatch.domain.product.product.repository.ProductLogRepository;
import com.ll.sbbatch.domain.product.product.repository.ProductRepository;
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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class MakeProductLogJobConfig {
    private final int CHUNK_SIZE = 50;
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
    public ItemReader<Product> step1Reader(){
        return new RepositoryItemReaderBuilder<Product>()
                .name("step1Reader")
                .repository(productRepository)
                .methodName("findAll")
                .pageSize(CHUNK_SIZE)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
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
