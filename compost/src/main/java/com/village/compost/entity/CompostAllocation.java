package com.village.compost.entity;

import com.village.compost.enums.AllocationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "compost_allocations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompostAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private CompostRequest compostRequest;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private CompostInventory inventory;

    @Column(nullable = false)
    private Double allocatedQuantity;

    private LocalDate allocatedDate;

    @Enumerated(EnumType.STRING)
    private AllocationStatus status;
}