package com.slc.mentoring.repository;

import com.slc.mentoring.entity.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<MatchResult, Long> {
    Optional<MatchResult> findByUser_UserId(Long userId);
}
