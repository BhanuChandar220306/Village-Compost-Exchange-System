package com.village.compost.service;

import com.village.compost.dto.request.CreatePickupRequest;
import com.village.compost.dto.response.PickupResponse;

import java.util.List;

public interface PickupService {

    PickupResponse createPickup(CreatePickupRequest request);

    List<PickupResponse> getAllPickups();

    List<PickupResponse> getMyPickups();

    List<PickupResponse> getTodayPickups();

    PickupResponse completePickup(Long id);
}