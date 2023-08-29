package com.example.springbatchtutorial.job.ValidatedParam;

import com.example.springbatchtutorial.job.ValidatedParam.Valiator.FileParamValiator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

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
//                .validator(new FileParamValiator())
                .validator(multiVaildator())
                .start(validatedParamStep1)
                .build();
    }

    //복수의 validator를 Array형태로 등록가능.
    private CompositeJobParametersValidator multiVaildator(){
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(new FileParamValiator()));
        return validator;
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
            //tasklet에서 파일 내용에 검증 가능하지만, Job단에서 검증가능.
            log.debug("validated Param Tasklet call!! paramter {}", fileName);
          return RepeatStatus.FINISHED;
        };
    }
}
