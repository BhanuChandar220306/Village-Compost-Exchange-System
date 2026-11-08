package com.village.compost.service;

import com.village.compost.dto.request.CreateWasteRequest;
import com.village.compost.dto.response.WasteResponse;

import java.util.List;

public interface WasteService {

    WasteResponse createWasteRequest(CreateWasteRequest request);

    List<WasteResponse> getMyRequests();

    List<WasteResponse> getAllRequests();

    WasteResponse deleteRequest(Long id);

    List<WasteResponse> getPendingRequests();

    WasteResponse acceptRequest(Long id);

    WasteResponse collectRequest(Long id);

}