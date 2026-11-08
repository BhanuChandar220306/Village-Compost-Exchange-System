package com.village.compost.service;

import com.village.compost.dto.request.AllocateRequest;
import com.village.compost.dto.response.AllocationResponse;

import java.util.List;

public interface AllocationService {

    AllocationResponse allocateCompost(AllocateRequest request);

    List<AllocationResponse> getAllAllocations();

    List<AllocationResponse> getMyAllocations();

    AllocationResponse completeAllocation(Long id);

}