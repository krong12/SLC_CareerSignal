package com.slc.mentoring.service;

import com.slc.mentoring.dto.request.ResultPostRequest;
import com.slc.mentoring.dto.response.ResultPostResponse;
import com.slc.mentoring.entity.MatchResult;
import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.MentorStatus;
import com.slc.mentoring.entity.User;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.repository.MentorRepository;
import com.slc.mentoring.repository.ResultRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
public class ResultService {
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;

    public ResultPostResponse showResult(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        MatchResult result = resultRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RESULT));
        return new ResultPostResponse(result);
    }

    public ResultPostResponse createResult(Long userId, ResultPostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        Mentor firstMentor = mentorRepository.findById(request.getFirstMentorId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
        Mentor secondMentor = mentorRepository.findById(request.getSecondMentorId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

        if(firstMentor.getMentorStatus() != MentorStatus.COMFIRMED
                || secondMentor.getMentorStatus() != MentorStatus.COMFIRMED)
            throw new CustomException(ExceptionCode.NOT_CONFIRMED_MENTOR);

        MatchResult result = MatchResult.builder()
                .user(user)
                .firstMentor(firstMentor)
                .secondMentor(secondMentor)
                .build();
        resultRepository.save(result);
        return new ResultPostResponse(result);
    }

    public ResultPostResponse updateResult(Long userId, ResultPostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        MatchResult result = resultRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RESULT));

        Mentor firstMentor = mentorRepository.findById(request.getFirstMentorId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
        Mentor secondMentor = mentorRepository.findById(request.getSecondMentorId())
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

        if(firstMentor.getMentorStatus() != MentorStatus.COMFIRMED
                || secondMentor.getMentorStatus() != MentorStatus.COMFIRMED)
            throw new CustomException(ExceptionCode.NOT_CONFIRMED_MENTOR);

        result.update(firstMentor, secondMentor);
        return new ResultPostResponse(result);
    }

    public void deleteResult(Long userId) {
        MatchResult result = resultRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RESULT));
        Long resultId = result.getResultId();
        resultRepository.deleteById(resultId);
    }

    public void createResultsByCSV(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드한 파일이 비었습니다.");
        }

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            Iterable<CSVRecord> csvRecords = csvFormat.parse(fileReader);

            for (CSVRecord record : csvRecords) {
                String userIdStr = record.get("userId");
                String firstMentorIdStr = record.get("firstMentorId");
                String secondMentorIdStr = record.get("secondMentorId");

                if (userIdStr == null || userIdStr.isBlank() ||
                        firstMentorIdStr == null || firstMentorIdStr.isBlank() ||
                        secondMentorIdStr == null || secondMentorIdStr.isBlank()) {
                    throw new IllegalArgumentException("CSV 파일의 형식이 올바르지 않습니다. (userId, firstMentorId, secondMentorId 필수)");
                }

                Long userId = Long.valueOf(userIdStr.trim());
                Long firstMentorId = Long.valueOf(firstMentorIdStr.trim());
                Long secondMentorId = Long.valueOf(secondMentorIdStr.trim());

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
                Mentor firstMentor = mentorRepository.findById(firstMentorId)
                        .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
                Mentor secondMentor = mentorRepository.findById(secondMentorId)
                        .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

                if (firstMentor.getMentorStatus() != MentorStatus.COMFIRMED
                        || secondMentor.getMentorStatus() != MentorStatus.COMFIRMED) {
                    throw new CustomException(ExceptionCode.NOT_CONFIRMED_MENTOR);
                }

                // 이미 해당 유저의 매칭 결과가 존재할 경우 업데이트, 없으면 생성
                MatchResult result = resultRepository.findByUser_UserId(userId)
                        .orElse(null);

                if (result != null) {
                    result.update(firstMentor, secondMentor);
                } else {
                    result = MatchResult.builder()
                            .user(user)
                            .firstMentor(firstMentor)
                            .secondMentor(secondMentor)
                            .build();
                    resultRepository.save(result);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV 파일 파싱 중 오류 발생 : " + e.getMessage(), e);
        }
    }
}
