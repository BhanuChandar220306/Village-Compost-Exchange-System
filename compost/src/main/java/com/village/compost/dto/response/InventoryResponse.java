package com.village.compost.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private Long id;

    private Double availableQuantity;

    private String compostType;

    private String location;
}