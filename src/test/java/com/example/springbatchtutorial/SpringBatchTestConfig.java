package com.example.springbatchtutorial;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : com.example.springbatchtutorial.job.HelloWorld
 * fileName       : SpringBatchTestConfig
 * author         : kmy
 * date           : 2023/08/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/30        kmy       최초 생성
 */
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class SpringBatchTestConfig {
}
