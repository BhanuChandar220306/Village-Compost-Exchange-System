package com.village.compost.service.impl;

import com.village.compost.dto.response.DashboardResponse;
import com.village.compost.entity.User;
import com.village.compost.entity.WasteRequest;
import com.village.compost.entity.CompostRequest;
import com.village.compost.entity.PickupSchedule;
import com.village.compost.repository.UserRepository;
import com.village.compost.repository.WasteRepository;
import com.village.compost.repository.CompostRequestRepository;
import com.village.compost.repository.PickupScheduleRepository;
import com.village.compost.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final WasteRepository wasteRepository;
    private final CompostRequestRepository compostRequestRepository;
    private final PickupScheduleRepository pickupScheduleRepository;

    private User getCurrentUser() {

        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public DashboardResponse getMyDashboard() {

        User user = getCurrentUser();

        String role = user.getRole().name();

        DashboardResponse.DashboardResponseBuilder response =
                DashboardResponse.builder()
                        .userId(user.getId())
                        .fullName(user.getFullName())
                        .role(role)
                        .rewardPoints(user.getRewardPoints());

        if (role.equals("HOUSEHOLD")) {

            var wasteRequests =
                    wasteRepository.findAll()
                            .stream()
                            .filter(w ->
                                    w.getHousehold().getId()
                                            .equals(user.getId()))
                            .toList();

            response
                    .totalWasteRequests(wasteRequests.size())
                    .pendingWasteRequests(
                            countStatus(wasteRequests, "PENDING"))
                    .acceptedWasteRequests(
                            countStatus(wasteRequests, "ACCEPTED"))
                    .collectedWasteRequests(
                            countStatus(wasteRequests, "COLLECTED"));
        }

        if (role.equals("COLLECTOR")) {

            var pickups =
                    pickupScheduleRepository
                            .findByCollector(user);

            response
                    .totalPickups(pickups.size())
                    .acceptedPickups(
                            countPickupStatus(pickups, "ACCEPTED"))
                    .completedPickups(
                            countPickupStatus(pickups, "COLLECTED"));
        }

        if (role.equals("FARMER")) {

            var requests =
                    compostRequestRepository
                            .findByFarmer(user);

            response
                    .totalCompostRequests(requests.size())
                    .pendingCompostRequests(
                            countCompostStatus(requests, "PENDING"))
                    .acceptedCompostRequests(
                            countCompostStatus(requests, "ACCEPTED"))
                    .rejectedCompostRequests(
                            countCompostStatus(requests, "REJECTED"));
        }

        return response.build();
    }

    @Override
    public DashboardResponse getAdminDashboard() {

        var users = userRepository.findAll();

        var wasteRequests = wasteRepository.findAll();

        var compostRequests = compostRequestRepository.findAll();

        var pickups = pickupScheduleRepository.findAll();

        return DashboardResponse.builder()

                .totalUsers(users.size())

                .totalHouseholds(
                        users.stream()
                                .filter(u ->
                                        u.getRole().name()
                                                .equals("HOUSEHOLD"))
                                .count())

                .totalFarmers(
                        users.stream()
                                .filter(u ->
                                        u.getRole().name()
                                                .equals("FARMER"))
                                .count())

                .totalCollectors(
                        users.stream()
                                .filter(u ->
                                        u.getRole().name()
                                                .equals("COLLECTOR"))
                                .count())

                .totalWasteRequests(wasteRequests.size())

                .pendingWasteRequests(
                        countStatus(wasteRequests, "PENDING"))

                .acceptedWasteRequests(
                        countStatus(wasteRequests, "ACCEPTED"))

                .collectedWasteRequests(
                        countStatus(wasteRequests, "COLLECTED"))

                .totalCompostRequests(compostRequests.size())

                .pendingCompostRequests(
                        countCompostStatus(
                                compostRequests, "PENDING"))

                .acceptedCompostRequests(
                        countCompostStatus(
                                compostRequests, "ACCEPTED"))

                .rejectedCompostRequests(
                        countCompostStatus(
                                compostRequests, "REJECTED"))

                .totalPickups(pickups.size())

                .acceptedPickups(
                        countPickupStatus(pickups, "ACCEPTED"))

                .completedPickups(
                        countPickupStatus(pickups, "COLLECTED"))

                .build();
    }

    private long countStatus(
            java.util.List<WasteRequest> requests,
            String status) {

        return requests.stream()
                .filter(r ->
                        r.getStatus() != null &&
                                r.getStatus().name().equals(status))
                .count();
    }

    private long countCompostStatus(
            java.util.List<CompostRequest> requests,
            String status) {

        return requests.stream()
                .filter(r ->
                        r.getStatus() != null &&
                                r.getStatus().name().equals(status))
                .count();
    }

    private long countPickupStatus(
            java.util.List<PickupSchedule> pickups,
            String status) {

        return pickups.stream()
                .filter(p ->
                        p.getStatus() != null &&
                                p.getStatus().name().equals(status))
                .count();
    }
}