package com.slc.mentoring.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPostRequest {
    private String studentId;
    private String passCode;
}
