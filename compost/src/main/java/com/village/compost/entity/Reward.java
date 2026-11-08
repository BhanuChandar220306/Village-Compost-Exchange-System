package com.village.compost.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Household receiving reward
    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private User household;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private String reason;

    private LocalDateTime earnedDate;
}