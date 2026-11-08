package com.village.compost.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePickupRequest {

    private Long wasteRequestId;

    private Long collectorId;

    private LocalDate pickupDate;

    private LocalTime pickupTime;
}