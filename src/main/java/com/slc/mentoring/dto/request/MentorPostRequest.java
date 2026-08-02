package com.slc.mentoring.dto.request;

import com.slc.mentoring.entity.Area;
import com.slc.mentoring.entity.CareerPath;
import com.slc.mentoring.entity.MentorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MentorPostRequest {
    private String name;
    private String companyName;
    private String job;
    private CareerPath careerPath;
    private Area area;
    private String foreignSchool;
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
