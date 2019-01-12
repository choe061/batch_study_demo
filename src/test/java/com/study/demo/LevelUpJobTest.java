package com.study.demo;

import com.study.demo.domain.enums.Level;
import com.study.demo.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
/**
 * Created by choi on 10/01/2019.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LevelUpJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 회원등급_조정_And_쿠폰발행_Test() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0, memberRepository.findAllByLevelEquals(Level.GOLD).size());
    }

    @Test
    public void findAllByLevelEquals_Test() {
        System.out.println("List<Member> GOLDs :: " + memberRepository.findAllByLevelEquals(Level.GOLD));

        System.out.println("List<Member> SILVERs :: " + memberRepository.findAllByLevelEquals(Level.SILVER));

        System.out.println("List<Member> BRONZEs :: " + memberRepository.findAllByLevelEquals(Level.BRONZE));
    }
}
