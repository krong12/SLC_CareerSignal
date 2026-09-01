package com.slc.mentoring.service;

import com.slc.mentoring.dto.request.RecordPostRequest;
import com.slc.mentoring.dto.response.RecordPostResponse;
import com.slc.mentoring.entity.*;
import com.slc.mentoring.entity.Record;
import com.slc.mentoring.global.error.CustomException;
import com.slc.mentoring.global.error.ExceptionCode;
import com.slc.mentoring.repository.MentorRepository;
import com.slc.mentoring.repository.QuestionRepository;
import com.slc.mentoring.repository.RecordRepository;
import com.slc.mentoring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final MentorRepository mentorRepository;
    private final QuestionRepository questionRepository;

    public RecordPostResponse showRecords(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        Record record = recordRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECORD));
        return new RecordPostResponse(record);
    }

    public RecordPostResponse CreateRecord(Long userId, RecordPostRequest request) {
        Long firstMentorId = request.getFirstMentorId();
        Long secondMentorId = request.getSecondMentorId();
        Long thirdMentorId = request.getThirdMentorId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        Mentor firstMentor = mentorRepository.findById(firstMentorId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
        Mentor secondMentor = mentorRepository.findById(secondMentorId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
        Mentor thirdMentor = mentorRepository.findById(thirdMentorId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

        if(firstMentor.getMentorStatus() != MentorStatus.COMFIRMED
                || secondMentor.getMentorStatus() != MentorStatus.COMFIRMED
                || thirdMentor.getMentorStatus() != MentorStatus.COMFIRMED)
            throw new CustomException(ExceptionCode.NOT_CONFIRMED_MENTOR);

        Record record = Record.builder()
                .user(user)
                .firstMentor(firstMentor)
                .secondMentor(secondMentor)
                .thirdMentor(thirdMentor)
                .drink(request.getDrink())
                .priorityAt(LocalDateTime.now())
                .build();
        recordRepository.save(record);

        if(request.getFirstQuestion() != null && !request.getFirstQuestion().trim().isEmpty()) {
            MentorQuestion firstQuestion = MentorQuestion.builder()
                    .user(user)
                    .mentor(firstMentor)
                    .content(request.getFirstQuestion())
                    .build();
            questionRepository.save(firstQuestion);
        }
        if(request.getSecondQuestion() != null && !request.getSecondQuestion().trim().isEmpty()) {
            MentorQuestion secondQuestion = MentorQuestion.builder()
                    .user(user)
                    .mentor(secondMentor)
                    .content(request.getSecondQuestion())
                    .build();
            questionRepository.save(secondQuestion);
        }
        if(request.getThirdQuestion() != null && !request.getThirdQuestion().trim().isEmpty()) {
            MentorQuestion thirdQuestion = MentorQuestion.builder()
                    .user(user)
                    .mentor(thirdMentor)
                    .content(request.getThirdQuestion())
                    .build();
            questionRepository.save(thirdQuestion);
        }

        return new RecordPostResponse(record);
    }

    public RecordPostResponse updateRecord(Long userId, RecordPostRequest request) {
        Long firstMentorId = request.getFirstMentorId();
        Long secondMentorId = request.getSecondMentorId();
        Long thirdMentorId = request.getThirdMentorId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        Mentor firstMentor = mentorRepository.findById(firstMentorId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
        Mentor secondMentor = mentorRepository.findById(secondMentorId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));
        Mentor thirdMentor = mentorRepository.findById(thirdMentorId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_MENTOR_ID));

        if(firstMentor.getMentorStatus() != MentorStatus.COMFIRMED
                || secondMentor.getMentorStatus() != MentorStatus.COMFIRMED
                || thirdMentor.getMentorStatus() != MentorStatus.COMFIRMED)
            throw new CustomException(ExceptionCode.NOT_CONFIRMED_MENTOR);

        Record record = recordRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECORD));
        record.update(user, firstMentor, secondMentor, thirdMentor, request.getDrink(), LocalDateTime.now());

        if(request.getFirstQuestion() != null && !request.getFirstQuestion().trim().isEmpty()) {
            MentorQuestion firstQuestion = MentorQuestion.builder()
                    .user(user)
                    .mentor(firstMentor)
                    .content(request.getFirstQuestion())
                    .build();
            questionRepository.save(firstQuestion);
        }
        if(request.getSecondQuestion() != null && !request.getSecondQuestion().trim().isEmpty()) {
            MentorQuestion secondQuestion = MentorQuestion.builder()
                    .user(user)
                    .mentor(secondMentor)
                    .content(request.getSecondQuestion())
                    .build();
            questionRepository.save(secondQuestion);
        }
        if(request.getThirdQuestion() != null && !request.getThirdQuestion().trim().isEmpty()) {
            MentorQuestion thirdQuestion = MentorQuestion.builder()
                    .user(user)
                    .mentor(thirdMentor)
                    .content(request.getThirdQuestion())
                    .build();
            questionRepository.save(thirdQuestion);
        }

        return new RecordPostResponse(record);
    }

    public void deleteRecord(Long userId) {
        Record record = recordRepository.findByUserId(userId)
                        .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECORD));
        Long recordId = record.getRecordId();
        recordRepository.deleteById(recordId);
    }
}
