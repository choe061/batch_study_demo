package com.study.demo.domain.enums;

import lombok.Getter;

/**
 * Created by choi on 10/01/2019.
 */
@Getter
public enum Grade {
    VIP(3, null),
    GOLD(2, VIP),
    FAMILY(1, GOLD);

    private final int value;
    private final Grade nextGrade;

    Grade(int value, Grade nextGrade) {
        this.value = value;
        this.nextGrade = nextGrade;
    }

    public static Grade valueOf(int value) {
        switch (value) {
            case 1:
                return FAMILY;
            case 2:
                return GOLD;
            case 3:
                return VIP;
            default:
                throw new NullPointerException("Unknown Grade : " + value);
        }
    }
}
