package com.example.springbatchtutorial.job.StepListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

/**
 * packageName    : com.example.springbatchtutorial.job.StepListener
 * fileName       : StepLoogerListener
 * author         : kmy
 * date           : 2023/08/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/30        kmy       최초 생성
 */
@Slf4j
public class StepLoogerListener {

    @BeforeStep
    public void beforeStep(StepExecution stepExecution){
        log.error("{} has begun!",stepExecution.getStepName());
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution){
        log.error("{} has ended!",stepExecution.getStepName());
        return stepExecution.getExitStatus();
    }

}
