package com.slc.mentoring.entity;

public enum Area {
    CAPITAL_AREA("수도권"),
    NON_CAPITAL_AREA("수도권외"),
    FOREIGN("해외");

    private final String description;
    Area(String description) { this.description = description; }
    public String getDescription() { return description; }
}
