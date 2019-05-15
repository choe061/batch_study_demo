package com.study.demo.repository;

import com.study.demo.domain.User;
import com.study.demo.domain.enums.Grade;
import com.study.demo.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by choi on 10/01/2019.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime localDateTime, UserStatus status);

    List<User> findByUpdatedDateBeforeAndStatusEqualsAndGradeEquals(LocalDateTime localDateTime, UserStatus status, Grade grade);

}
