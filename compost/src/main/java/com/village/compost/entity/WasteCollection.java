package com.village.compost.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "waste_collections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pickup request
    @OneToOne
    @JoinColumn(name = "waste_request_id", nullable = false)
    private WasteRequest wasteRequest;

    // Collector
    @ManyToOne
    @JoinColumn(name = "collector_id", nullable = false)
    private User collector;

    // Compost Centre
    @ManyToOne
    @JoinColumn(name = "compost_center_id", nullable = false)
    private User compostCenter;

    @Column(nullable = false)
    private Double collectedWeight;

    private LocalDateTime collectionDate;

    private String photoUrl;

    @Column(length = 500)
    private String remarks;
}