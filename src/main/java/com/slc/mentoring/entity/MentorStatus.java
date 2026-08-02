package com.slc.mentoring.entity;

public enum MentorStatus {
    PREPARE("예비"),
    CONTACTING("섭외중"),
    COMFIRMED("섭외확정"),
    DECLINED("미참여"),
    PRIVATE("비공개");

    private final String description;
    MentorStatus(String description) { this.description = description; }
    public String getDescription() { return description; }
}
