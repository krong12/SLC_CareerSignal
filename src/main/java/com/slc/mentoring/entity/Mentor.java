package com.slc.mentoring.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mentorId;

    @Column(nullable = false)
    private String name; // 표시이름

    @Column(nullable = false)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mentor_major",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "major_id")
    )
    private List<Major> major = new ArrayList<>(); // 전공

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mentor_field", joinColumns = @JoinColumn(name = "mentor_id"))
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private List<Field> field = new ArrayList<>(); // 분야

    @Column(nullable = true)
    private String companyName; // 회사명

    @Column(nullable = false)
    private String job; // 직무

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CareerPath careerPath; // 진로단계 : 1 = 학사취업, 2 = 석사취업, 3 = 박사취업, 4 = 기타

    @Column(nullable = false)
    private String areaName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Area area; // 근무지역 : 1 = 수도권, 2 = 수도권외, 3 = 해외

    @Column(nullable = false)
    private boolean foreignSchool; // 유학 여부

    @Column(nullable = false)
    private boolean majorRelated; // 전공일치 여부

    @Column(nullable = false)
    private Long graduateYear; // 졸업년도

    @Column(nullable = false, columnDefinition = "TEXT")
    private String introduce; // 약력, 주요 경력 소개

    @Column(nullable = true)
    private String linkedin; // 링크드인 공개 안하는 경우 null

    @Column(nullable = true)
    private String profileImagePath; // 프로필 이미지 주소, 공개안하면 null

    @Column(nullable = false)
    private boolean profileRelease; // 프로필 공개 동의 여부

    @Column(nullable = false)
    private boolean voteRelease; // 투표 노출 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentorStatus mentorStatus; // 멘토 상태 : 1 = 예비, 2 = 섭외중, 3 = 섭외확정, 4 = 미참여, 5 = 비공개

    @Column(nullable = false)
    private Long mentorLimit; // 정원

    @Column(nullable = false)
    private boolean limitRelease; // 정원 공개 여부

    @Column(nullable = false)
    private boolean remainRelease; // 잔여 인원 공개 여부

    @Builder
    public Mentor(String name, List<Major> major, List<Field> field, String companyName, String job, CareerPath careerPath, String areaName, Area area,
                  boolean foreignSchool, boolean majorRelated, Long graduateYear, String introduce,
                  String linkedin, String profileImagePath, boolean profileRelease, boolean voteRelease,
                  MentorStatus mentorStatus, Long mentorLimit, boolean limitRelease, boolean remainRelease) {
        this.name = name;
        this.major = major;
        this.field = field;
        this.companyName = companyName;
        this.job = job;
        this.careerPath = careerPath;
        this.areaName = areaName;
        this.area = area;
        this.foreignSchool = foreignSchool;
        this.majorRelated = majorRelated;
        this.graduateYear = graduateYear;
        this.introduce = introduce;
        this.linkedin = linkedin;
        this.profileImagePath = profileImagePath;
        this.profileRelease = profileRelease;
        this.voteRelease = voteRelease;
        this.mentorStatus = mentorStatus;
        this.mentorLimit = mentorLimit;
        this.limitRelease = limitRelease;
        this.remainRelease = remainRelease;
    }

    public void update(String name, List<Major> major, List<Field> field, String companyName, String job, CareerPath careerPath, String areaName, Area area,
                       boolean foreignSchool, boolean majorRelated, Long graduateYear, String introduce,
                       String linkedin, String profileImagePath, boolean profileRelease, boolean voteRelease,
                       MentorStatus mentorStatus, Long mentorLimit, boolean limitRelease, boolean remainRelease) {
        this.name = name;
        this.major.clear();
        if(major != null) { this.major.addAll(major); }
        this.field = field;
        this.companyName = companyName;
        this.job = job;
        this.careerPath = careerPath;
        this.areaName = areaName;
        this.area = area;
        this.foreignSchool = foreignSchool;
        this.majorRelated = majorRelated;
        this.graduateYear = graduateYear;
        this.introduce = introduce;
        this.linkedin = linkedin;
        this.profileImagePath = profileImagePath;
        this.profileRelease = profileRelease;
        this.voteRelease = voteRelease;
        this.mentorStatus = mentorStatus;
        this.mentorLimit = mentorLimit;
        this.limitRelease = limitRelease;
        this.remainRelease = remainRelease;
    }
}
