package com.village.compost.service;

import com.village.compost.dto.response.DashboardResponse;

public interface DashboardService {

    DashboardResponse getMyDashboard();

    DashboardResponse getAdminDashboard();
}