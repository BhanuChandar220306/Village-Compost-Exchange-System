package com.village.compost.entity;

import com.village.compost.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "compost_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompostRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;

    private Double quantity; // kg

    private LocalDate requiredDate;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}