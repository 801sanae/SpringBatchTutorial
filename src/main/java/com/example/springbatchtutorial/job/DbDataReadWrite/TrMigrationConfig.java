package com.example.springbatchtutorial.job.DbDataReadWrite;

import com.example.springbatchtutorial.domain.Accounts;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
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
    public Step trMigrationStep(ItemReader trOrdersReader, ItemProcessor trOrderProcessor, ItemWriter trOrderWriter){
        return stepBuilderFactory.get("trMigrationStep")
                .<Orders, Accounts>chunk(5) // <src,trg> commit 단위 5
                .reader(trOrdersReader)
                .processor(trOrderProcessor)
                .writer(trOrderWriter)
                //                .writer((items)->{
//                    items.forEach(System.out::println);
//                })
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

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrderProcessor(){
//        return new ItemProcessor<Orders, Accounts>() {
//            @Override
//            public Accounts process(Orders item) throws Exception {
//                return null;
//            }
//        }
        return (item) ->{
            return new Accounts(item);
        };
    }

    @StepScope
    @Bean
    public ItemWriter<Accounts> trOrderWriter(){
        // #1 RepositoryItemWriterBuilder를 사용하여 적재가능.
//        return new RepositoryItemWriterBuilder<Accounts>()
//                .repository(accountsRepository)
//                .methodName("save")
//                .build();
        // #2 ItemWriter<Accounts>를 사용하여 직접 지정하여 적재가능.
        return (items)->{
            items.forEach( item -> accountsRepository.save(item) );
        };
    }




}
