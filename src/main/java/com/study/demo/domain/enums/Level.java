package com.study.demo.domain.enums;

import lombok.Getter;

/**
 * Created by choi on 10/01/2019.
 */
@Getter
public enum Level {
    GOLD(3, null),
    SILVER(2, GOLD),
    BRONZE(1, SILVER);

    private final int value;
    private final Level nextLevel;

    Level(int value, Level nextLevel) {
        this.value = value;
        this.nextLevel = nextLevel;
    }

    public static Level valueOf(int value) {
        switch (value) {
            case 1: return BRONZE;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new NullPointerException("Unknown Level : " + value);
        }
    }
}
