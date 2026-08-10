package com.slc.mentoring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long majorId;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    public Major(String name) {
        this.name = name;
    }

    // 테스트코드용
    public Major(Long majorId, String name) {
        this.majorId = majorId;
        this.name = name;
    }
}
