package com.slc.mentoring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserGetResponse {
    private List<UserPostResponse> user_list;
}
