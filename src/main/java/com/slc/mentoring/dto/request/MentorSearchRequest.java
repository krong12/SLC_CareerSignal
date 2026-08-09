package com.slc.mentoring.dto.request;

import com.slc.mentoring.entity.CareerPath;
import com.slc.mentoring.entity.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MentorSearchRequest {
    private List<String> majorNames;
    private List<Field> fields;
    private List<CareerPath> careerPaths;
    private List<Boolean> foreignSchools;
    private List<Boolean> majorRelated;
}
