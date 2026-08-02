package com.slc.mentoring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MentorGetResponse {
    private List<MentorPostResponse> mentor_list;
}
