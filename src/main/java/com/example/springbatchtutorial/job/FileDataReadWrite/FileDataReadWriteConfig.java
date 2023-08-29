package com.example.springbatchtutorial.job.FileDataReadWrite;


import com.example.springbatchtutorial.domain.Player;
import com.example.springbatchtutorial.domain.PlayerYears;
import com.example.springbatchtutorial.mapper.PlayerFieldSetMapper;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FileDataReadWriteConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;

    @Autowired private StepBuilderFactory stepBuilderFactory;
//--spring.batch.job.names=fileDataReadWriteJob
    @Bean
    public Job fileDataReadWriteJob(Step fileDataReadWriteStep){
        return jobBuilderFactory.get("fileDataReadWriteJob")
                .incrementer(new RunIdIncrementer())
                .start(fileDataReadWriteStep)
                .build();
    }

    @JobScope
    @Bean
    public Step fileDataReadWriteStep(ItemReader playerFlatFileItemReader,
                                      ItemProcessor playerToPlayerYearsItemProcessor,
                                      ItemWriter playerYearsItemWriter){

        return stepBuilderFactory.get("fileDataReadWriteStep")
                .<Player, PlayerYears>chunk(5)
                .reader(playerFlatFileItemReader)
                .processor(playerToPlayerYearsItemProcessor)
                .writer(playerYearsItemWriter)
//                .writer((itmes)-> itmes.forEach(System.out::println))
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerFlatFileItemReader(){
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerFlatFileItemReader")
                .resource(new FileSystemResource("Players.csv"))
                .lineTokenizer(new DelimitedLineTokenizer(","))
                .fieldSetMapper(new PlayerFieldSetMapper())
                .linesToSkip(1)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Player,PlayerYears> playerToPlayerYearsItemProcessor(){
        return (player) -> new PlayerYears(player);
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerYearsItemWriter(){
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"ID","lastName","position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource("players_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerYearsItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();

    }


}
