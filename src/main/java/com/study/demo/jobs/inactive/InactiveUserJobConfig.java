package com.study.demo.jobs.inactive;

import com.study.demo.domain.User;
import com.study.demo.domain.enums.Grade;
import com.study.demo.domain.enums.UserStatus;
import com.study.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by choi on 10/01/2019.
 */
@Configuration
@RequiredArgsConstructor
public class InactiveUserJobConfig {

    private final UserRepository userRepository;
    private final EntityManagerFactory entityManagerFactory;

    @Value("${batch-demo.chunk.size}")
    private int CHUNK_SIZE;

    @Bean
    public Job inactiveUserJob(JobBuilderFactory jobBuilderFactory, Step inactiveJobStep) {
        return jobBuilderFactory.get("inactiveUserJob")
                                .preventRestart()
                                .start(inactiveJobStep)
                                .build();
    }

    @Bean
    public Step inactiveJobStep(StepBuilderFactory stepBuilderFactory, ListItemReader<User> inactiveUserReader) {
        return stepBuilderFactory.get("inactiveUserStep")
                                 .<User, User> chunk(CHUNK_SIZE)
                                 .reader(inactiveUserReader)
                                 .processor(inactiveUserProcessor())
                                 .writer(inactiveUserWriter())
                                 .build();
    }

    @Bean
    @StepScope
    public ListItemReader<User> inactiveUserReader(@Value("#{jobParameters[nowDate]}") Date nowDate) {
        LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());
        List<User> inactiveUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals(now.minusYears(1), UserStatus.ACTIVE);
        return new ListItemReader<>(inactiveUsers);
    }

//    @Bean(destroyMethod = "")
//    @StepScope
//    public JpaPagingItemReader<User> inactiveUserJpaReader() {
//        JpaPagingItemReader<User> jpaPagingItemReader = new JpaPagingItemReader() {
//            @Override
//            public int getPage() {
//                return 0;
//            }
//        };
//        jpaPagingItemReader.setQueryString("SELECT u FROM user AS u WHERE u.updatedDate < :updatedDate and u.status = :status");
//
//        Map<String, Object> map = new HashMap<>();
//        LocalDateTime now = LocalDateTime.now();
//        map.put("updatedDate", now.minusYears(1));
//        map.put("status", UserStatus.ACTIVE);
//
//        jpaPagingItemReader.setParameterValues(map);
//        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
//        jpaPagingItemReader.setPageSize(CHUNK_SIZE);
//        return jpaPagingItemReader;
//    }

    private ItemProcessor<? super User, ? extends User> inactiveUserProcessor() {
        return User::setInactive;
    }

    private JpaItemWriter<User> inactiveUserWriter() {
        JpaItemWriter<User> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }



//    @Bean
//    public Job levelUpJob(JobBuilderFactory jobBuilderFactory, Step levelUpJobStep, Step giveCouponJobStep) {
//        return jobBuilderFactory.get("levelUpJob")
//                .preventRestart()
//                .start(levelUpJobStep)
//                .next(giveCouponJobStep)
//                .build();
//    }
//
//    @Bean
//    public Step levelUpJobStep(StepBuilderFactory stepBuilderFactory) {
//        return stepBuilderFactory.get("levelUpJobStep")
//                .<User, User>chunk(10)
//                .reader(levelUpReader())
//                .processor(levelUpProcessor())
//                .writer(levelUpWriter())
//                .build();
//    }
//
//    @Bean
//    public Step giveCouponJobStep(StepBuilderFactory stepBuilderFactory) {
//        return stepBuilderFactory.get("sendCouponJobStep")
//                .chunk(10)
////                .reader()
////                .processor()
////                .writer()
//                .build();
//    }

//    @Bean
//    @StepScope
//    public ItemReader<? extends User> levelUpReader() {
//        List<User> users = userRepository.findAll();
//        return new ListItemReader<>(users);
//    }

//    private ItemProcessor<? super User, ? extends User> levelUpProcessor() {
//        return items -> levelUp(items);

//        return new ItemProcessor<User, User>() {
//            @Override
//            public User process(User item) throws Exception {
//                return null;
//            }
//        };
//    }

//    private User levelUp(User item) {
//        if (item.getLoginCount() < item.getGrade().getValue() * 10) {
//            return item;
//        }
//
//        Optional<Grade> nextLevel = Optional.ofNullable(item.getGrade().getNextGrade());
//        item.setGrade(nextLevel.orElse(Grade.VIP));
//        return item;
//    }

//    private ItemWriter<? super User> levelUpWriter() {
//        return items -> userRepository.saveAll(items);

        /** 아래 세 가지 모두 같은 표현 */
//        return (ItemWriter<User>) items -> memberRepository.saveAll(items);

//        return new ItemWriter<User>() {
//            @Override
//            public void write(List<? extends User> items) throws Exception {
//                memberRepository.saveAll(items)
//            }
//        };

//        return ((List<? extends User> members) -> memberRepository.saveAll(members));
//    }

}
