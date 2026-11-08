package com.village.compost.entity;

import com.village.compost.enums.QualityGrade;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "compost_production")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompostProduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Compost Centre
    @ManyToOne
    @JoinColumn(name = "compost_center_id", nullable = false)
    private User compostCenter;

    @Column(nullable = false, unique = true)
    private String batchNumber;

    @Column(nullable = false)
    private Double wasteUsed;

    @Column(nullable = false)
    private Double compostProduced;

    @Column(nullable = false)
    private LocalDate productionDate;

    @Enumerated(EnumType.STRING)
    private QualityGrade qualityGrade;
}