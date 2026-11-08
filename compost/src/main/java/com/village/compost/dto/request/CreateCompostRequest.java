package com.village.compost.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCompostRequest {

    @NotNull
    private Double quantity;

    @Future
    @NotNull
    private LocalDate requiredDate;
}