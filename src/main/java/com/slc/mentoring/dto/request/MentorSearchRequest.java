package com.slc.mentoring.dto.request;

import com.slc.mentoring.entity.Area;
import com.slc.mentoring.entity.CareerPath;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MentorSearchRequest {
    private CareerPath careerPath;
    private Area area;
    private Boolean foreignSchool;
    private Boolean majorRelated;
    private Long graduateYear;
}
