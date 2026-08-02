package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPostResponse {
    private Long userId;
    private String studentId;

    public UserPostResponse(User user) {
        this.userId = user.getUserId();
        this.studentId = user.getStudentId();
    }
}
