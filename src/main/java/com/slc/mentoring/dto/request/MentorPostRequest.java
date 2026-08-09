package com.slc.mentoring.dto.request;

import com.slc.mentoring.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MentorPostRequest {
    private String name;
    private List<String> major;
    private List<Field> field;
    private String companyName;
    private String job;
    private CareerPath careerPath;
    private String areaName;
    private Area area;
    private boolean foreignSchool;
    private boolean majorRelated;
    private Long graduatedYear;
    private String introduce;
    private String linkedin;
    private String profileImagePath;
    private boolean profileRelease;
    private boolean voteRelease;
    private MentorStatus mentorStatus;
    private Long mentorLimit;
    private boolean limitRelease;
    private boolean remainRelease;
}
