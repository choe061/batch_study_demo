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
@Configuration
public class LevelUpJobConfig {

    private final MemberRepository memberRepository;

    public LevelUpJobConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public Job levelUpJob(JobBuilderFactory jobBuilderFactory, Step levelUpJobStep, Step giveCouponJobStep) {
        return jobBuilderFactory.get("levelUpJob")
                .preventRestart()
                .start(levelUpJobStep)
                .next(giveCouponJobStep)
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
    public Step giveCouponJobStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("sendCouponJobStep")
                .chunk(10)
//                .reader()
//                .processor()
//                .writer()
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

//    private void giveCoupon(Member member) {
//    }

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
