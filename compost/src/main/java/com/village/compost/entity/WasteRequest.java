package com.village.compost.entity;

import com.village.compost.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "waste_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Household who created the request
    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private User household;

    @Column(nullable = false)
    private String wasteType;

    @Column(nullable = false)
    private Double estimatedWeight;

    @Column(nullable = false)
    private LocalDate pickupDate;

    @Column(length = 500)
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private User collector;

    private LocalDate collectedDate;
}