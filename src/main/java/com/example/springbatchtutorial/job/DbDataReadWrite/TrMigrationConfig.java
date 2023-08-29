package com.example.springbatchtutorial.job.DbDataReadWrite;

import com.example.springbatchtutorial.domain.Orders;
import com.example.springbatchtutorial.repository.AccountsRepository;
import com.example.springbatchtutorial.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

/**
 * packageName    : com.example.springbatchtutorial.job.DbDataReadWrite
 * fileName       : TrMigrationConfig
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
@RequiredArgsConstructor
public class TrMigrationConfig {
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    @Autowired private OrdersRepository ordersRepository;
    @Autowired private AccountsRepository accountsRepository;
//--spring.batch.job.names=trMigrationJob
    @Bean
    public Job trMigrationJob(Step trMigrationStep){
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(ItemReader trOrdersReader){
        return stepBuilderFactory.get("trMigrationStep")
                .<Orders,Orders>chunk(5) // <src,trg> commit 단위 5
                .reader(trOrdersReader)
                .writer((items)->{
                    items.forEach(System.out::println);
                })
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader(){
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(ordersRepository)
                .methodName("findAll")
                .pageSize(5)//chunk size가 동일하게~
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

}
