package com.study.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by choi on 08/01/2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int eventLogNo;

    @Column
    private String couponName;

    @Column
    private LocalDateTime publishedDate;

}
