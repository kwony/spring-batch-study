package com.example.studybatch;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

public class CustomItemReader implements ItemReader<String> {
    int index = 0;

    private ThreadLocal<StepExecution> stepExecution = new ThreadLocal<>();

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution.set(stepExecution);
    }

    @Override
    public String read() {
        ExecutionContext executionContext = stepExecution.get().getExecutionContext();

        String[] partitionData = (String[]) executionContext.get("data");

        if (index < partitionData.length) {
            return partitionData[index++];
        } else {
            return null;
        }
    }
}