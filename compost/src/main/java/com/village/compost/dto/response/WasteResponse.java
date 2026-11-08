package com.village.compost.dto.response;

import com.village.compost.enums.RequestStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteResponse {

    private Long id;

    private String householdName;

    private String wasteType;

    private Double estimatedWeight;

    private LocalDate pickupDate;

    private String description;

    private String imageUrl;

    private RequestStatus status;
}