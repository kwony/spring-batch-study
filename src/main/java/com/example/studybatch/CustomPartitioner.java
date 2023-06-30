package com.example.studybatch;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class CustomPartitioner implements Partitioner {

    private final String[] data = {"Hello World", "1 2 3 4 5", "카리나 윈터", "지젤 닝닝 지효 정연"};

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();
        int partitionSize = data.length / gridSize;

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();

            int length = (i == (gridSize - 1) ? data.length : (i + 1) * partitionSize) - i * partitionSize;
            String[] partitionData = new String[length];
            System.arraycopy(data, i * partitionSize, partitionData, 0, length);

            context.put("data", partitionData);
            partitions.put("partition" + i, context);
        }
        return partitions;
    }

}