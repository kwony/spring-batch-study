package com.example.studybatch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

public class CustomItemReader implements ItemReader<String> {
    int index = 0;

    private final String[] data = {"1", "2 3", "4 5 6", "7 8 9 10"};

    private ThreadLocal<StepExecution> stepExecution = new ThreadLocal<>();

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution.set(stepExecution);
    }

    @Override
    public String read() {
        if (index < data.length) {
            return data[index++];
        }
        return null;

//        ExecutionContext executionContext = stepExecution.get().getExecutionContext();
//
//        String[] partitionData = (String[]) executionContext.get("data");
//
//        if (index < partitionData.length) {
//            return partitionData[index++];
//        } else {
//            return null;
//        }
    }
}