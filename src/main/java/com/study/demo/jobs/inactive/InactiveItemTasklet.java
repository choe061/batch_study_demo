package com.study.demo.jobs.inactive;

import com.study.demo.domain.User;
import com.study.demo.domain.enums.UserStatus;
import com.study.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tasklet 을 이용한 방식
 *  - "단일 작업"으로 만드는 개념
 *  - 청크 지향 프로세싱이 아닌 방식
 *      - 읽기 -> 처리 -> 쓰기
 *      - 세 단계로 나뉜 방식이 청크 지향 프로세싱
 */
@Component
@RequiredArgsConstructor
public class InactiveItemTasklet implements Tasklet {

    private final UserRepository userRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // reader
        Date nowDate = (Date) chunkContext.getStepContext().getJobParameters().get("nowDate");
        LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());

        List<User> inactiveUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals(now.minusYears(1), UserStatus.ACTIVE);

        // processor
        inactiveUsers = inactiveUsers.stream()
                                     .map(User::setInactive)
                                     .collect(Collectors.toList());

        // writer
        userRepository.saveAll(inactiveUsers);
        return RepeatStatus.FINISHED;
    }
}
