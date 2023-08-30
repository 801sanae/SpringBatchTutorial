package com.example.springbatchtutorial.job.StepListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * packageName    : com.example.springbatchtutorial.job.StepListener
 * fileName       : ChunkLoggerListener
 * author         : kmy
 * date           : 2023/08/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/30        kmy       최초 생성
 */
@Slf4j
public class ChunkLoggerListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext context){
        log.error("{} has begun!", context.getStepContext().getStepName());
    }

    @AfterChunk
    public void afterChunk(ChunkContext context){
        log.error("{} has ended!", context.getStepContext().getStepName());
    }
}
