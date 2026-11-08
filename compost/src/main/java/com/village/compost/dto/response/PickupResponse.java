package com.village.compost.dto.response;

import com.village.compost.enums.PickupStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupResponse {

    private Long id;

    private Long wasteRequestId;

    private String householdName;

    private String collectorName;

    private LocalDate pickupDate;

    private LocalTime pickupTime;

    private PickupStatus status;
}