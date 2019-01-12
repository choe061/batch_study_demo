package com.study.demo.repository;

import com.study.demo.domain.Member;
import com.study.demo.domain.enums.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by choi on 10/01/2019.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByLevelEquals(Level level);

}
