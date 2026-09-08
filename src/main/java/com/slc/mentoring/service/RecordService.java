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
import java.util.HashMap;
import java.util.Map;

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
        Record record = recordRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECORD));

        if(!isComplete(record))
            throw new CustomException(ExceptionCode.NOT_FOUND_RECORD);

        return responseWithQuestions(record);
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

        Drink drink = request.getDrink() == null ? Drink.NOMATTER : request.getDrink();

        Record record = recordRepository.findByUser_UserId(userId)
                .map(existingRecord -> {
                    existingRecord.update(user, firstMentor, secondMentor, thirdMentor,
                            drink, LocalDateTime.now());
                    return existingRecord;
                })
                .orElseGet(() -> Record.builder()
                        .user(user)
                        .firstMentor(firstMentor)
                        .secondMentor(secondMentor)
                        .thirdMentor(thirdMentor)
                        .drink(drink)
                        .priorityAt(LocalDateTime.now())
                        .build());
        recordRepository.save(record);
        clearQuestions(userId);
        saveQuestion(user, firstMentor, request.getFirstQuestion());
        saveQuestion(user, secondMentor, request.getSecondQuestion());
        saveQuestion(user, thirdMentor, request.getThirdQuestion());

        return responseWithQuestions(record);
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

        Record record = recordRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECORD));
        record.update(user, firstMentor, secondMentor, thirdMentor, request.getDrink(), LocalDateTime.now());

        clearQuestions(userId);
        saveQuestion(user, firstMentor, request.getFirstQuestion());
        saveQuestion(user, secondMentor, request.getSecondQuestion());
        saveQuestion(user, thirdMentor, request.getThirdQuestion());

        return responseWithQuestions(record);
    }

    private void saveQuestion(User user, Mentor mentor, String content) {
        if(content == null || content.trim().isEmpty()) return;
        MentorQuestion question = MentorQuestion.builder()
                .user(user)
                .mentor(mentor)
                .content(content.trim())
                .build();
        questionRepository.save(question);
    }

    private void clearQuestions(Long userId) {
        questionRepository.deleteAllByUser_UserId(userId);
        questionRepository.flush();
    }

    private RecordPostResponse responseWithQuestions(Record record) {
        Map<Long, String> questionsByMentor = new HashMap<>();
        questionRepository.findAllByUser_UserIdOrderByQuestionIdAsc(record.getUser().getUserId())
                .forEach(question -> questionsByMentor.put(
                        question.getMentor().getMentorId(), question.getContent()));

        return new RecordPostResponse(
                record,
                questionsByMentor.get(record.getFirstMentor().getMentorId()),
                questionsByMentor.get(record.getSecondMentor().getMentorId()),
                questionsByMentor.get(record.getThirdMentor().getMentorId())
        );
    }

    public void deleteQuestions(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER_ID));
        recordRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECORD));
        clearQuestions(userId);
    }

    public void deleteRecord(Long userId) {
        Record record = recordRepository.findByUser_UserId(userId)
                        .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_RECORD));
        Long recordId = record.getRecordId();
        recordRepository.deleteById(recordId);
    }

    private boolean isComplete(Record record) {
        return record.getFirstMentor() != null
                && record.getSecondMentor() != null
                && record.getThirdMentor() != null;
    }
}
