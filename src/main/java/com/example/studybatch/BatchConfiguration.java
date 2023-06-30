package com.example.studybatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public ItemReader<String> fileItemReader() {
        return new CustomItemReader();
    }

    @Bean
    public ItemProcessor<String, WordCount> wordCountProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<WordCount> wordCountWriter() {
        return new CustomItemWriter();
    }

    @Bean
    public Step slaveStep(ItemReader<String> reader, ItemProcessor<String, WordCount> processor,
                          ItemWriter<WordCount> writer) {
        return stepBuilderFactory.get("slaveStep")
                .<String, WordCount>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step masterStep(Partitioner partitioner, Step slaveStep) {
        return stepBuilderFactory.get("masterStep")
                .partitioner("slaveStep", partitioner)
                .step(slaveStep)
                .gridSize(2)
                .taskExecutor(taskExecutor())
                .build();
    }

//    @Bean
//    public Job masterSlaveJob(Step masterStep) {
//        return jobBuilderFactory.get("masterSlaveJob")
//                .incrementer(new RunIdIncrementer())
//                .start(masterStep)
//                .build();
//    }

    @Bean
    public Step singleStep(ItemReader<String> reader, ItemProcessor<String, WordCount> processor, ItemWriter<WordCount> writer) {
        return stepBuilderFactory.get("singleStep")
                .<String, WordCount>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job singleJob(Step singleStep) {
        return jobBuilderFactory.get("singleJob")
                .start(singleStep)
                .build();
    }

    @Bean
    @StepScope
    public Partitioner partitioner() {
        return new CustomPartitioner();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setQueueCapacity(10);
        return taskExecutor;
    }


}
