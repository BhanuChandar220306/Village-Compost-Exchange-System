package com.village.compost.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "compost_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompostInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double availableQuantity;

    private String compostType;

    private String location;
}