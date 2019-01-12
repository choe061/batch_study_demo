package com.study.demo.jobs;

import com.study.demo.domain.Member;
import com.study.demo.domain.enums.Level;
import com.study.demo.jobs.readers.QueueItemReader;
import com.study.demo.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * Created by choi on 10/01/2019.
 */
@AllArgsConstructor
@Configuration
public class LevelUpJobConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public Job levelUpJob(JobBuilderFactory jobBuilderFactory, Step levelUpJobStep) {
        return jobBuilderFactory.get("levelUpJob")
                .preventRestart()
                .start(levelUpJobStep)
//                .next(otherStep)
                .build();
    }

    @Bean
    public Step levelUpJobStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("levelUpJobStep")
                .<Member, Member> chunk(10)
                .reader(levelUpReader())
                .processor(levelUpProcessor())
                .writer(levelUpWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<? extends Member> levelUpReader() {
        List<Member> members = memberRepository.findAll();
        return new QueueItemReader<>(members);
    }

    private ItemProcessor<? super Member, ? extends Member> levelUpProcessor() {
        return items -> levelUp(items);

//        return new ItemProcessor<Member, Member>() {
//            @Override
//            public Member process(Member item) throws Exception {
//                return null;
//            }
//        };
    }

    private Member levelUp(Member item) {
        if (item.getLoginCount() < item.getLevel().getValue() * 10) {
            return item;
        }

        Optional<Level> nextLevel = Optional.ofNullable(item.getLevel().getNextLevel());
        item.setLevel(nextLevel.orElse(Level.GOLD));
        return item;
    }

    private void giveCoupon(Member member) {
        // 근데 등급 조정이랑 쿠폰 제공은 애초에 job을 나눠야 할 것 같은데...?
    }

    private ItemWriter<? super Member> levelUpWriter() {
        return items -> memberRepository.saveAll(items);

        /** 아래 세 가지 모두 같은 표현 */
//        return (ItemWriter<Member>) items -> memberRepository.saveAll(items);

//        return new ItemWriter<Member>() {
//            @Override
//            public void write(List<? extends Member> items) throws Exception {
//                memberRepository.saveAll(items)
//            }
//        };

//        return ((List<? extends Member> members) -> memberRepository.saveAll(members));
    }

}
