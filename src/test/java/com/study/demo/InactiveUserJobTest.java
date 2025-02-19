package com.study.demo;

import com.study.demo.domain.enums.UserStatus;
import com.study.demo.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by choi on 10/01/2019.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class InactiveUserJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("휴면 회원 전환 테스트")
    @Test
    public void test_inactiveUserJob() throws Exception {
        Date now = new Date();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder().addDate("nowDate", now).toJobParameters()
        );

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(11, userRepository.findAll().size());
        assertEquals(0, userRepository.findByUpdatedDateBeforeAndStatusEquals(LocalDateTime.now().minusYears(1), UserStatus.ACTIVE).size());
    }

}
