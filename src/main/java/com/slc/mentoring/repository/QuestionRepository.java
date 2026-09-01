package com.slc.mentoring.repository;

import com.slc.mentoring.entity.MentorQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<MentorQuestion, Long> {
}
