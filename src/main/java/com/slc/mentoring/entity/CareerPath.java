package com.slc.mentoring.entity;

public enum CareerPath {
    GRADUATE("학사 취업"),
    MASTER("석사 취업"),
    DOCTOR("박사 취업"),
    ETC("기타");

    private final String description;
    CareerPath(String description) { this.description = description; }
    public String getDescription() { return description; }
}
