
package com.village.compost.service.impl;

import com.village.compost.dto.request.CreateCompostRequest;
import com.village.compost.dto.response.CompostRequestResponse;
import com.village.compost.entity.CompostRequest;
import com.village.compost.entity.User;
import com.village.compost.enums.RequestStatus;
import com.village.compost.enums.UserRole;
import com.village.compost.repository.CompostRequestRepository;
import com.village.compost.repository.UserRepository;
import com.village.compost.service.CompostService;
import com.village.compost.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompostServiceImpl implements CompostService {

    private final CompostRequestRepository compostRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void notifyAdmins(String message) {
        userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == UserRole.ADMIN)
                .forEach(admin ->
                        notificationService.createNotification(
                                admin.getId(), message
                        )
                );
    }

    @Override
    public CompostRequestResponse createRequest(
            CreateCompostRequest request) {

        User farmer = getCurrentUser();

        CompostRequest compostRequest = CompostRequest.builder()
                .farmer(farmer)
                .quantity(request.getQuantity())
                .requiredDate(request.getRequiredDate())
                .status(RequestStatus.PENDING)
                .build();

        CompostRequest saved =
                compostRequestRepository.save(compostRequest);

        notifyAdmins(
                "New compost request submitted by "
                        + farmer.getFullName()
                        + ". Quantity: " + request.getQuantity()
        );

        return map(saved);
    }

    @Override
    public List<CompostRequestResponse> myRequests() {

        User farmer = getCurrentUser();

        return compostRequestRepository.findByFarmer(farmer)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<CompostRequestResponse> allRequests() {

        return compostRequestRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public CompostRequestResponse approveRequest(Long id) {

        CompostRequest request = compostRequestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.ACCEPTED);

        CompostRequest saved = compostRequestRepository.save(request);

        notificationService.createNotification(
                request.getFarmer().getId(),
                "Your compost request has been approved."
        );

        return map(saved);
    }

    @Override
    public CompostRequestResponse rejectRequest(Long id) {

        CompostRequest request = compostRequestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);

        CompostRequest saved = compostRequestRepository.save(request);

        notificationService.createNotification(
                request.getFarmer().getId(),
                "Your compost request has been rejected."
        );

        return map(saved);
    }

    private CompostRequestResponse map(CompostRequest request) {

        return CompostRequestResponse.builder()
                .id(request.getId())
                .farmerName(request.getFarmer().getFullName())
                .quantity(request.getQuantity())
                .requiredDate(request.getRequiredDate())
                .status(request.getStatus())
                .build();
    }
}
