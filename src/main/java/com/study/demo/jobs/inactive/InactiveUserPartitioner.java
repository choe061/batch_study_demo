package com.study.demo.jobs.inactive;

import com.study.demo.domain.enums.Grade;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Partitioner 인터페이스의 구현체
 * 1. SimplePartitioner
 * 2. MultiResourcePartitioner
 * 두 구현체를 사용하는 방법
 * 3. 인터페이스를 구현/사용하는 방법
 *     - InactiveUserPartitioner : 회원 등급에 따라 파티션을 분할하는 코드
 */
public class InactiveUserPartitioner implements Partitioner {

    private static final String GRADE = "grade";
    private static final String INACTIVE_USER_TASK = "InactiveUserTask";

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> map = new HashMap<>(gridSize);
        Grade[] grades = Grade.values();
        for (int i = 0; i < grades.length; i++) {
            ExecutionContext context = new ExecutionContext();
            context.putString(GRADE, grades[i].name());
            map.put(INACTIVE_USER_TASK + i, context);
        }
        return map;
    }
}
