package com.example.studybatch;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<String, WordCount> {
    @Override
    public WordCount process(String item) {
        String[] words = item.trim().split("\\s+");
        return new WordCount(words.length);
    }
}