package com.village.compost.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private Long userId;
    private String fullName;
    private String role;

    // Waste statistics
    private long totalWasteRequests;
    private long pendingWasteRequests;
    private long acceptedWasteRequests;
    private long collectedWasteRequests;

    // Compost statistics
    private long totalCompostRequests;
    private long pendingCompostRequests;
    private long acceptedCompostRequests;
    private long rejectedCompostRequests;

    // Pickup statistics
    private long totalPickups;
    private long acceptedPickups;
    private long completedPickups;

    // Rewards
    private Integer rewardPoints;

    // Overall users
    private long totalUsers;
    private long totalHouseholds;
    private long totalFarmers;
    private long totalCollectors;
}