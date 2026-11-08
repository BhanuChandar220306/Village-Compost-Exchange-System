package com.village.compost.service;

import com.village.compost.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getMyNotifications();

    long getUnreadCount();

    NotificationResponse markAsRead(Long id);

    void createNotification(Long userId, String message);
}