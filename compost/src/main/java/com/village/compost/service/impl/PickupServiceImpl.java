package com.village.compost.service.impl;

import com.village.compost.dto.request.CreatePickupRequest;
import com.village.compost.dto.response.PickupResponse;
import com.village.compost.entity.PickupSchedule;
import com.village.compost.entity.User;
import com.village.compost.entity.WasteRequest;
import com.village.compost.enums.PickupStatus;
import com.village.compost.enums.RequestStatus;
import com.village.compost.repository.PickupScheduleRepository;
import com.village.compost.repository.UserRepository;
import com.village.compost.repository.WasteRepository;
import com.village.compost.service.NotificationService;
import com.village.compost.service.PickupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PickupServiceImpl implements PickupService {

    private final PickupScheduleRepository pickupScheduleRepository;
    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public PickupResponse createPickup(CreatePickupRequest request) {

        WasteRequest wasteRequest = wasteRepository
                .findById(request.getWasteRequestId())
                .orElseThrow(() ->
                        new RuntimeException("Waste request not found"));

        User collector = userRepository
                .findById(request.getCollectorId())
                .orElseThrow(() ->
                        new RuntimeException("Collector not found"));

        PickupSchedule pickup = PickupSchedule.builder()
                .wasteRequest(wasteRequest)
                .collector(collector)
                .pickupDate(request.getPickupDate())
                .pickupTime(request.getPickupTime())
                .status(PickupStatus.SCHEDULED)
                .build();

        PickupSchedule saved =
                pickupScheduleRepository.save(pickup);

        // Notify collector
        notificationService.createNotification(
                collector.getId(),
                "New waste pickup scheduled for "
                        + request.getPickupDate()
                        + " at "
                        + request.getPickupTime()
        );

        // Notify household
        notificationService.createNotification(
                wasteRequest.getHousehold().getId(),
                "Your waste pickup has been scheduled successfully."
        );

        return map(saved);
    }


    @Override
    public List<PickupResponse> getAllPickups() {

        return pickupScheduleRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }


    @Override
    public List<PickupResponse> getMyPickups() {

        User collector =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return pickupScheduleRepository
                .findByCollector(collector)
                .stream()
                .map(this::map)
                .toList();
    }


    @Override
    public List<PickupResponse> getTodayPickups() {

        return pickupScheduleRepository
                .findByPickupDate(LocalDate.now())
                .stream()
                .map(this::map)
                .toList();
    }


    @Override
    public PickupResponse completePickup(Long id) {

        PickupSchedule pickup =
                pickupScheduleRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Pickup not found"
                                )
                        );


        /*
         * Get the waste request connected
         * to this pickup.
         */
        WasteRequest wasteRequest =
                pickup.getWasteRequest();


        if (wasteRequest == null) {

            throw new RuntimeException(
                    "Waste request not found for this pickup"
            );
        }


        /*
         * Prevent rewarding the same waste
         * request more than once.
         */
        if (wasteRequest.getStatus()
                == RequestStatus.COLLECTED) {

            throw new RuntimeException(
                    "This waste request has already been collected"
            );
        }


        /*
         * Mark pickup as completed.
         */
        pickup.setStatus(
                PickupStatus.COMPLETED
        );


        /*
         * Mark waste request as collected.
         */
        wasteRequest.setStatus(
                RequestStatus.COLLECTED
        );


        /*
         * Get the household that created
         * the waste request.
         */
        User household =
                wasteRequest.getHousehold();


        if (household == null) {

            throw new RuntimeException(
                    "Household not found for this waste request"
            );
        }


        /*
         * Give household 20 reward points.
         */
        household.setRewardPoints(
                household.getRewardPoints() + 20
        );


        /*
         * Save everything.
         */
        userRepository.save(household);

        wasteRepository.save(wasteRequest);

        PickupSchedule savedPickup =
                pickupScheduleRepository.save(pickup);


        /*
         * Notify household that the pickup
         * has been completed.
         */
        notificationService.createNotification(
                household.getId(),
                "Your waste has been collected successfully. "
                        + "You received 20 reward points."
        );


        return map(savedPickup);
    }


    private PickupResponse map(
            PickupSchedule pickup
    ) {

        return PickupResponse.builder()
                .id(pickup.getId())
                .wasteRequestId(
                        pickup.getWasteRequest().getId()
                )
                .householdName(
                        pickup.getWasteRequest()
                                .getHousehold()
                                .getFullName()
                )
                .collectorName(
                        pickup.getCollector()
                                .getFullName()
                )
                .pickupDate(
                        pickup.getPickupDate()
                )
                .pickupTime(
                        pickup.getPickupTime()
                )
                .status(
                        pickup.getStatus()
                )
                .build();
    }
}