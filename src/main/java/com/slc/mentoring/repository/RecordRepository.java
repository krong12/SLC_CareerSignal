package com.slc.mentoring.repository;

import com.slc.mentoring.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findByUser_UserId(Long userId);
}
