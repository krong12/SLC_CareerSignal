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
public class MentorQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorId", nullable = false)
    private Mentor mentor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder
    public MentorQuestion(User user, Mentor mentor, String content) {
        this.user = user;
        this.mentor = mentor;
        this.content = content;
    }
}
