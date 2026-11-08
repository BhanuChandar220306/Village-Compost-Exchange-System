package com.village.compost.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateWasteRequest {

    @NotBlank
    private String wasteType;

    @NotNull
    private Double estimatedWeight;

    @NotNull
    @FutureOrPresent
    private LocalDate pickupDate;

    private String description;

    private String imageUrl;
}