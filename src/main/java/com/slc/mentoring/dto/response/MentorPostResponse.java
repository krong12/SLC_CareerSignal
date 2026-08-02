package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.Area;
import com.slc.mentoring.entity.CareerPath;
import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.MentorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MentorPostResponse {
    private Long mentorId;
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

    public MentorPostResponse(Mentor mentor) {
        this.mentorId = mentor.getMentorId();
        this.name = mentor.getName();
        this.companyName = mentor.getCompanyName();
        this.job = mentor.getJob();
        this.careerPath = mentor.getCareerPath();
        this.area = mentor.getArea();
        this.foreignSchool = mentor.getForeignSchool();
        this.majorRelated = mentor.isMajorRelated();
        this.graduatedYear = mentor.getGraduateYear();
        this.introduce = mentor.getIntroduce();
        this.linkedin = mentor.getLinkedin();
        this.profileImagePath = mentor.getProfileImagePath();
        this.profileRelease = mentor.isProfileRelease();
        this.voteRelease = mentor.isVoteRelease();
        this.mentorStatus = mentor.getMentorStatus();
        this.mentorLimit = mentor.getMentorLimit();
        this.limitRelease = mentor.isLimitRelease();
        this.remainRelease = mentor.isRemainRelease();
    }
}
