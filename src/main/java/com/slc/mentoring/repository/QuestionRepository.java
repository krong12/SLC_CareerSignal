package com.slc.mentoring.repository;

import com.slc.mentoring.entity.MentorQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<MentorQuestion, Long> {
    long deleteAllByUser_UserId(Long userId);
    long countByUser_UserId(Long userId);
    List<MentorQuestion> findAllByUser_UserIdOrderByQuestionIdAsc(Long userId);
}
