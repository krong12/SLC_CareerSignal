package com.slc.mentoring.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_mentorId")
    private Mentor firstMentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_mentorId")
    private Mentor secondMentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_mentorId")
    private Mentor thirdMentor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Drink drink;

    @Column(nullable = false)
    private LocalDateTime priorityAt; // 제출 시각

    @Builder
    public Record(User user, Mentor firstMentor, Mentor secondMentor, Mentor thirdMentor,
                  Drink drink, LocalDateTime priorityAt) {
        this.user = user;
        this.firstMentor = firstMentor;
        this.secondMentor = secondMentor;
        this.thirdMentor = thirdMentor;
        this.drink = drink;
        this.priorityAt = priorityAt;
    }

    public void update(User user, Mentor firstMentor, Mentor secondMentor, Mentor thirdMentor,
                       Drink drink, LocalDateTime priorityAt) {
        this.user = user;
        this.firstMentor = firstMentor;
        this.secondMentor = secondMentor;
        this.thirdMentor = thirdMentor;
        this.drink = drink;
        this.priorityAt = priorityAt;
    }
}
