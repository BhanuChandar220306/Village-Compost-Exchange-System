package com.village.compost.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocateRequest {

    private Long compostRequestId;

    private Long inventoryId;

    private Double quantity;

}