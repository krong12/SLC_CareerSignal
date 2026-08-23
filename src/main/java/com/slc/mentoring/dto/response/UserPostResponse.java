package com.slc.mentoring.dto.response;

import com.slc.mentoring.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPostResponse {
    private Long userId;
    private String studentId;
    private String name;

    public UserPostResponse(User user) {
        this.userId = user.getUserId();
        this.studentId = user.getStudentId();
        this.name = user.getName();
    }
}
