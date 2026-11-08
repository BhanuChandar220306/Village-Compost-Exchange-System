package com.village.compost.service.impl;
import java.util.List;
import com.village.compost.dto.request.CreateWasteRequest;
import com.village.compost.dto.response.WasteResponse;
import com.village.compost.entity.User;
import com.village.compost.entity.WasteRequest;
import com.village.compost.enums.RequestStatus;
import com.village.compost.repository.WasteRepository;
import com.village.compost.repository.UserRepository;
import com.village.compost.service.WasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class WasteServiceImpl implements WasteService {

    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;

    @Override
    public WasteResponse createWasteRequest(CreateWasteRequest request) {

        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        WasteRequest waste = WasteRequest.builder()
                .household(user)
                .wasteType(request.getWasteType())
                .estimatedWeight(request.getEstimatedWeight())
                .pickupDate(request.getPickupDate())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .status(RequestStatus.PENDING)
                .build();

        WasteRequest saved = wasteRepository.save(waste);

        return WasteResponse.builder()
                .id(saved.getId())
                .wasteType(saved.getWasteType())
                .estimatedWeight(saved.getEstimatedWeight())
                .pickupDate(saved.getPickupDate())
                .description(saved.getDescription())
                .imageUrl(saved.getImageUrl())
                .status(saved.getStatus())
                .build();
    }
    private User getCurrentUser() {

        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

    }
    @Override
    public List<WasteResponse> getMyRequests() {

        User user = getCurrentUser();

        return wasteRepository.findByHousehold(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public List<WasteResponse> getAllRequests() {

        return wasteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public WasteResponse deleteRequest(Long id) {

        User currentUser = getCurrentUser();

        WasteRequest waste = wasteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found"));

        if (!waste.getHousehold().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException(
                    "You are not allowed to delete this request"
            );
        }

        if (waste.getStatus() != RequestStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending requests can be deleted"
            );
        }

        wasteRepository.delete(waste);

        return mapToResponse(waste);
    }
    private WasteResponse mapToResponse(WasteRequest waste) {

        return WasteResponse.builder()
                .id(waste.getId())
                .householdName(waste.getHousehold().getFullName())
                .wasteType(waste.getWasteType())
                .estimatedWeight(waste.getEstimatedWeight())
                .pickupDate(waste.getPickupDate())
                .description(waste.getDescription())
                .status(waste.getStatus())
                .build();
    }
    @Override
    public List<WasteResponse> getPendingRequests() {

        return wasteRepository.findByStatus(RequestStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }
    @Override
    public WasteResponse acceptRequest(Long id) {

        WasteRequest waste = wasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        waste.setStatus(RequestStatus.ACCEPTED);

        wasteRepository.save(waste);

        return mapToResponse(waste);

    }
    @Override
    public WasteResponse collectRequest(Long id) {

        WasteRequest waste = wasteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        waste.setStatus(RequestStatus.COLLECTED);

        User household = waste.getHousehold();

        household.setRewardPoints(
                household.getRewardPoints() + 20
        );

        userRepository.save(household);

        wasteRepository.save(waste);

        return mapToResponse(waste);

    }
}