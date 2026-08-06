package com.slc.mentoring.entity;

import java.util.Arrays;

public enum CareerPath {
    COMPANY("기업 재직"),
    MASTER("대학원 재학"),
    RESEARCH("대학원 졸업 후 연구"),
    PUBLIC("공공기관"),
    CEO("창업(사업)"),
    FREELANCER("프리랜서"),
    PREPARING("미래 준비 중"),
    ETC("기타");

    private final String description;
    CareerPath(String description) { this.description = description; }
    public String getDescription() { return description; }

    public static CareerPath fromDescription(String description) {
        if(description == null || description.isBlank()) return ETC;
        return Arrays.stream(CareerPath.values())
                .filter(path -> path.description.equals(description.trim()))
                .findFirst()
                .orElse(CareerPath.ETC);
    }
}
