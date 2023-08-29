package com.example.springbatchtutorial.job.MultipleStep;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job MultipleStepJob(Step multipleStep1, Step multipleStep2, Step multipleStep3){
        return jobBuilderFactory.get("MultipleStepJob")
                .incrementer(new RunIdIncrementer())
                .start(multipleStep1)
                .next(multipleStep2)
                .next(multipleStep3)
                .build();

    }

    @JobScope
    @Bean
    public Step multipleStep1(){
        return stepBuilderFactory.get("multipleStep1")
                .tasklet((a,b) -> {
                    log.debug("Step1");
            return RepeatStatus.FINISHED;
        }).build();
    }

    @JobScope
    @Bean
    public Step multipleStep2(){
        return stepBuilderFactory.get("multipleStep2")
                .tasklet((stepContribution,chunkContext)->{
                    log.debug("Step2 start");

                    ExecutionContext context = chunkContext
                                                .getStepContext()
                                                .getStepExecution()
                                                .getJobExecution()
                                                .getExecutionContext();

                    context.put("Name","KMY");

                    log.debug("Step2 finish");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @JobScope
    @Bean
    public Step multipleStep3(){
        return stepBuilderFactory.get("multipleStep3")
                .tasklet((stepContribution,chunkContext)->{
                    log.debug("Step3 start");

                    ExecutionContext context = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    log.debug("step2 data -> {}", context.get("Name"));

                    log.debug("Step3 finish");
                    return RepeatStatus.FINISHED;
                }).build();
    }




}
