package com.slc.mentoring.entity;

public enum Area {
    CAPITAL_AREA("수도권"),
    NON_CAPITAL_AREA("수도권외"),
    FOREIGN("해외");

    private final String description;
    Area(String description) { this.description = description; }
    public String getDescription() { return description; }

    public static Area fromDescription(String description) {
        if(description == null || description.isBlank()) return null;
        if(description.equals("서울") || description.equals("경기")) return CAPITAL_AREA;
        if(description.equals("해외")) return FOREIGN;
        return NON_CAPITAL_AREA;
    }
}
