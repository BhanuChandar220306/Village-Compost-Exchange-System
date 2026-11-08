package com.village.compost.dto.response;

import com.village.compost.enums.RequestStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompostRequestResponse {

    private Long id;

    private String farmerName;

    private Double quantity;

    private LocalDate requiredDate;

    private RequestStatus status;
}