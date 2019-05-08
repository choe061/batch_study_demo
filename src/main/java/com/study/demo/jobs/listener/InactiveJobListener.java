package com.study.demo.jobs.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * Job 의 전후 처리 뿐만 아니라
 * Step 의 전후 처리,
 * 각 청크 단위에서의 전후 처리
 * 등등...
 * 세세한 과정 실행 시 특정 로직을 할당할 수 있다.
 */
@Slf4j
@Component
public class InactiveJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("\n\nBefore Job\n\n");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("\n\nAfter Job\n\n");
    }

}
