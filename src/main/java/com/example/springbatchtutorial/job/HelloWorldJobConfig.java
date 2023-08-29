package com.example.springbatchtutorial.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.example.springbatchtutorial.job
 * fileName       : HelloWorldJobConfig
 * author         : kmy
 * date           : 2023/08/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/29        kmy       최초 생성
 */
@Slf4j
@Configuration
public class HelloWorldJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
//--spring.batch.job.names=HelloWorldJob
    @Bean
    public Job HelloWorldJob(){
        return jobBuilderFactory.get("HelloWorldJob")
                .incrementer(new RunIdIncrementer())
                .start(Step1())
                .build();
    }

    @Bean
    public Step Step1(){
        return stepBuilderFactory.get("HelloWorldStep")
                .tasklet((stepContribution,chunkContext)->{
                    log.info("Hello World Batch!");
                  return RepeatStatus.FINISHED;
                }).build();
    }
}
