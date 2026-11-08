package com.village.compost.entity;

import com.village.compost.enums.PickupStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "pickup_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "waste_request_id", nullable = false)
    private WasteRequest wasteRequest;

    @ManyToOne
    @JoinColumn(name = "collector_id", nullable = false)
    private User collector;

    private LocalDate pickupDate;

    private LocalTime pickupTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PickupStatus status;
}