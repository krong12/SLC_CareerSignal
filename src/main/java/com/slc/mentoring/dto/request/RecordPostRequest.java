package com.slc.mentoring.dto.request;

import com.slc.mentoring.entity.Drink;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecordPostRequest {
    private Long firstMentorId;
    private String firstQuestion;
    private Long secondMentorId;
    private String secondQuestion;
    private Long thirdMentorId;
    private String thirdQuestion;
    private Drink drink;
}