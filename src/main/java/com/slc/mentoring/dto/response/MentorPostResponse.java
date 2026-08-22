package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MentorPostResponse {
    private Long mentorId;
    private String name;
    private String alias;
    private List<Major> major;
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
    private String cardImagePath;
    private boolean profileRelease;
    private boolean voteRelease;
    private MentorStatus mentorStatus;
    private Long mentorLimit;
    private boolean limitRelease;
    private boolean remainRelease;
    private String oneLine;

    public MentorPostResponse(Mentor mentor) {
        this.mentorId = mentor.getMentorId();
        this.alias = mentor.getAlias();
        this.name = mentor.getName();
        this.major = mentor.getMajor();
        this.field = mentor.getField();
        this.companyName = mentor.getCompanyName();
        this.job = mentor.getJob();
        this.careerPath = mentor.getCareerPath();
        this.areaName = mentor.getAreaName();
        this.area = mentor.getArea();
        this.foreignSchool = mentor.isForeignSchool();
        this.majorRelated = mentor.isMajorRelated();
        this.graduatedYear = mentor.getGraduateYear();
        this.introduce = mentor.getIntroduce();
        this.linkedin = mentor.getLinkedin();
        this.profileImagePath = mentor.getProfileImagePath();
        this.cardImagePath = mentor.getCardImagePath();
        this.profileRelease = mentor.isProfileRelease();
        this.voteRelease = mentor.isVoteRelease();
        this.mentorStatus = mentor.getMentorStatus();
        this.mentorLimit = mentor.getMentorLimit();
        this.limitRelease = mentor.isLimitRelease();
        this.remainRelease = mentor.isRemainRelease();
        this.oneLine = mentor.getOneLine();
    }
}
