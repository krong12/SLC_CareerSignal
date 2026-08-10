package com.slc.mentoring.service;

import com.slc.mentoring.dto.request.UserPostRequest;
import com.slc.mentoring.dto.response.UserGetResponse;
import com.slc.mentoring.dto.response.UserPostResponse;
import com.slc.mentoring.entity.User;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    public void createUserByCSV(MultipartFile file) {
        if(file.isEmpty())
            throw new IllegalArgumentException("업로드한 파일이 비었습니다.");
        List<UserPostRequest> userPostRequests = new ArrayList<>();
        try(BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();
            Iterable<CSVRecord> csvRecords = csvFormat.parse(fileReader);

            for(CSVRecord record : csvRecords) {
                UserPostRequest request = new UserPostRequest(
                        record.get("studentId"),
                        record.get("passCode")
                );
                userPostRequests.add(request);
            }

            for(UserPostRequest request : userPostRequests)
                signup(request);

        } catch (Exception e) {
            throw new RuntimeException("CSV 파일 파싱 중 오류 발생 : " + e.getMessage(), e);
        }
        return;
    }
}
