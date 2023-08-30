package com.example.springbatchtutorial.job.HelloWorld;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.config.Task;

import java.util.concurrent.Callable;

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
    public Job HelloWorldJob(Step callableStep2){
        return jobBuilderFactory.get("HelloWorldJob")
                .incrementer(new RunIdIncrementer())
                .start(Step1())
                .next(callableStep2)
                .build();
    }

    @Bean
    public Step Step1(){
        return stepBuilderFactory.get("HelloWorldStep")
                .tasklet((stepContribution,chunkContext)->{
                    log.debug("Hello World Batch!");
                  return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step callableStep2(Tasklet callableTasklet){
        return stepBuilderFactory.get("callableStep2")
                .tasklet(callableTasklet)
                .build();
    }

    /*
        Step이 실행되는 Thread와 별개의 스레드로 실행된다
        (Callable 객체가 RepeatStatus 객체 반환전에 완료된것으로 간주하지 않음)
        (callableStep2 Step가 병렬 실행되는것은 아님)
     */
    @Bean
    public Callable<RepeatStatus> callableObject(){
        return () ->{
          log.debug("this was execute in another thread");
          return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public CallableTaskletAdapter callableTasklet(){
        CallableTaskletAdapter callableTaskletAdapter = new CallableTaskletAdapter();
        callableTaskletAdapter.setCallable(callableObject());
        return callableTaskletAdapter;
    }

}
