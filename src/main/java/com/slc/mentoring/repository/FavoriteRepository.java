package com.slc.mentoring.repository;

import com.slc.mentoring.entity.Favorite;
import com.slc.mentoring.entity.Mentor;
import com.slc.mentoring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUserAndMentor(User user, Mentor mentor);
    List<Favorite> findAllByUser_UserId(Long userId);
    Favorite findByUser_UserIdAndMentor_MentorId(Long userId, Long mentorId);
}
