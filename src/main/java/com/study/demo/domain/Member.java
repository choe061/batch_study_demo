package com.study.demo.domain;

import com.study.demo.domain.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by choi on 08/01/2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@ToString
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int memberNo;

    @Column
    private String memberId;

    @Column
    private int loginCount;

    @Column
    @Enumerated(EnumType.STRING)
    private Level level;

}
