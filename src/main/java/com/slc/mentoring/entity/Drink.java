package com.slc.mentoring.entity;

import java.util.Arrays;

public enum Drink {
    WATER("물"),
    AMERICANO("아메리카노"),
    ICETEA("아이스티"),
    TEA("차"),
    NOMATTER("상관없음");

    private final String description;
    Drink(String description) { this.description = description; }
    public String getDescription() { return description; }
}
