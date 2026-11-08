package com.village.compost.dto.response;

import com.village.compost.enums.AllocationStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocationResponse {

    private Long id;

    private String farmerName;

    private Double allocatedQuantity;

    private LocalDate allocatedDate;

    private AllocationStatus status;

}