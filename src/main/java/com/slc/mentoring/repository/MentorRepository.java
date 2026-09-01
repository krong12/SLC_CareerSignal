package com.slc.mentoring.repository;

import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.MentorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByMentorStatus(MentorStatus mentorStatus);
}
