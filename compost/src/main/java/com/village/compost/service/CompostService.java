package com.village.compost.service;

import com.village.compost.dto.request.CreateCompostRequest;
import com.village.compost.dto.response.CompostRequestResponse;

import java.util.List;

public interface CompostService {

    CompostRequestResponse createRequest(CreateCompostRequest request);

    List<CompostRequestResponse> myRequests();

    List<CompostRequestResponse> allRequests();

    CompostRequestResponse approveRequest(Long id);

    CompostRequestResponse rejectRequest(Long id);
}