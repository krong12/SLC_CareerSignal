package com.slc.mentoring.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class MentorAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorId")
    private Mentor mentor;

    @Column(nullable = false)
    private int assignmentRank;

    @Column(nullable = false)
    private boolean status; // 배정상태

    @Column(nullable = false)
    private boolean isAuto; // true면 자동배정, false면 수동배정

    private LocalDateTime confirmedAt;

    @PrePersist
    public void prePersist() {
        this.confirmedAt = LocalDateTime.now();
    }
}
