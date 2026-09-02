package com.slc.mentoring.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class MatchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_mentorId")
    private Mentor firstMentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_mentorId")
    private Mentor secondMentor;

    @Builder
    public MatchResult(User user, Mentor firstMentor, Mentor secondMentor) {
        this.user = user;
        this.firstMentor = firstMentor;
        this.secondMentor = secondMentor;
    }

    public void update(Mentor firstMentor, Mentor secondMentor) {
        this.firstMentor = firstMentor;
        this.secondMentor = secondMentor;
    }
}
