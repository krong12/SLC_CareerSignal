package com.slc.mentoring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResultPostRequest {
    private Long firstMentorId;
    private Long secondMentorId;
}
