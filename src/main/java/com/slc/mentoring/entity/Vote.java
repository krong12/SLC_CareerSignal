package com.slc.mentoring.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
        name = "votes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_mentor", columnNames = {"userId", "mentorId"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorId")
    private Mentor mentor;

    @Column(nullable = false)
    private boolean isFinal; // true면 최종제출, false면 임시선택

    @Column(nullable = false)
    private boolean isValid; // 유효여부

    @Builder
    public Vote(User user, Mentor mentor, boolean isFinal) {
        this.user = user;
        this.mentor = mentor;
        this.isFinal = isFinal;
        this.isValid = true;
    }

    public void update_vote() {
        this.isFinal = true;
    }
}
