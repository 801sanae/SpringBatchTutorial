package com.example.springbatchtutorial.job.JobListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * packageName    : com.example.springbatchtutorial.job.JobListener
 * fileName       : JobLoggerListener
 * author         : kmy
 * date           : 2023/08/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/29        kmy       최초 생성
 */
@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MSG = "[{}] Job is Start";
    private static String AFTER_MSG = "[{}] Job is Finish. (status = {})";

    // 실행전
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.debug(BEFORE_MSG, jobExecution.getJobInstance().getJobName());
    }

    // 실행후
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.debug(AFTER_MSG, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        if(jobExecution.getStatus() == BatchStatus.FAILED){
            log.error("job is Failed");
        }

    }
}
