package com.slc.mentoring.service;

import com.slc.mentoring.dto.request.UserPostRequest;
import com.slc.mentoring.dto.response.UserGetResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.entity.User;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserPostResponse login(UserPostRequest userPostRequest) {
        String studentId = userPostRequest.getStudentId();
        String passCode = userPostRequest.getPassCode();

        User user = userRepository.findByStudentId(studentId) // db에 존재하지 않는 학번일 경우 에러코드 발송
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));

        if(!user.getPassCode().equals(passCode)) { // 패스코드가 일치하지 않을경우 에러코드 발송
            throw new CustomException(ExceptionCode.NOT_MATCHED_PASSCODE);
        }

        return new UserPostResponse(user.getUserId(), user.getStudentId());
    }

    public UserPostResponse signup(UserPostRequest userPostRequest) {
        User user = new User(userPostRequest.getStudentId(), userPostRequest.getPassCode());
        User savedUser = userRepository.save(user);
        return new UserPostResponse(savedUser);
    }

    public UserGetResponse showUsers() {
        List<User> users = userRepository.findAll();
        List<UserPostResponse> userList = users.stream()
                .map(UserPostResponse::new)
                .toList();
        return new UserGetResponse(userList);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
