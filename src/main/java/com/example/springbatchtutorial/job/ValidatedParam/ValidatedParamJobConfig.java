package com.example.springbatchtutorial.job.ValidatedParam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.example.springbatchtutorial.job
 * fileName       : ValidatedParamJobConfig
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
public class ValidatedParamJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
//--spring.batch.job.names=ValidatedParamJob
    @Bean
    public Job validatedParamJob(Step validatedParamStep1){
        return jobBuilderFactory.get("ValidatedParamJob")
                .incrementer(new RunIdIncrementer())
                .start(validatedParamStep1)
                .build();
    }

    @JobScope
    @Bean
    public Step validatedParamStep1(Tasklet validatedParamTasklet){
        return stepBuilderFactory.get("ValidatedParamStep")
                .tasklet(validatedParamTasklet)
                .build();
    }
    //--spring.batch.job.names=ValidatedParamJob -fileName=test.csv
    @StepScope
    @Bean
    public Tasklet validatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName){
        return (a,b) -> {
            log.debug("validated Param Tasklet call!! paramter {}", fileName);
          return RepeatStatus.FINISHED;
        };
    }
}
