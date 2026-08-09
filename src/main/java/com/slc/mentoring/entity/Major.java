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
}
