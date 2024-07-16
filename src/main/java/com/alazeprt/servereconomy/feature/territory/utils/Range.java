package com.alazeprt.servereconomy.feature.territory.utils;

import java.math.BigDecimal;

public class Range {

    private final BigDecimal min;

    private final BigDecimal max;

    public Range(BigDecimal min, BigDecimal max) {
        this.max = max;
        this.min = min;
    }

    public boolean inRange(BigDecimal object) {
        if(max.compareTo(BigDecimal.valueOf(-1)) == 0) return object.compareTo(min) >= 0;
        return object.compareTo(min) >= 0 && object.compareTo(max) <= 0;
    }
}
