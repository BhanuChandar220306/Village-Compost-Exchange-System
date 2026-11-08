package com.village.compost.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddInventoryRequest {

    private Double availableQuantity;

    private String compostType;

    private String location;
}