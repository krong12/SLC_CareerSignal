package com.slc.mentoring.repository;

import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.User;
import com.slc.mentoring.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserAndMentor(User user, Mentor mentor); // 특정 유저가 이미 같은 멘토를 투표한 적 있는지 확인
    List<Vote> findAllByUser_UserId(Long userId);
    Vote findByUser_UserIdAndMentor_MentorId(Long userId, Long mentorId);
    Long countByUser_UserIdAndIsFinal(Long userId, boolean isFinal);
    Long countByMentor_MentorIdAndIsFinal(Long mentorId, boolean isFinal);
}
