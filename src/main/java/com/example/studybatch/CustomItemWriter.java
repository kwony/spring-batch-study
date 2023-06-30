package com.example.studybatch;

import com.example.studybatch.BatchConfiguration;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomItemWriter implements ItemWriter<WordCount> {
    @Override
    public void write(List<? extends WordCount> items) {
        for (WordCount wordCount : items) {
            System.out.println("Thread name " + Thread.currentThread().getName());
            System.out.println("Word Count: " + wordCount.getCount());
        }
    }
}