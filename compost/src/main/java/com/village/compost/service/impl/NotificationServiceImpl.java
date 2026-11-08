package com.village.compost.service.impl;

import com.village.compost.dto.response.NotificationResponse;
import com.village.compost.entity.Notification;
import com.village.compost.entity.User;
import com.village.compost.repository.NotificationRepository;
import com.village.compost.repository.UserRepository;
import com.village.compost.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {

        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Override
    public List<NotificationResponse> getMyNotifications() {

        User user = getCurrentUser();

        return notificationRepository
                .findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public long getUnreadCount() {

        User user = getCurrentUser();

        return notificationRepository
                .countByUserAndReadFalse(user);
    }

    @Override
    public NotificationResponse markAsRead(Long id) {

        User user = getCurrentUser();

        Notification notification = notificationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized notification");
        }

        notification.setRead(true);

        return mapToResponse(
                notificationRepository.save(notification)
        );
    }

    @Override
    public void createNotification(Long userId, String message) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .title("Village Compost Notification")
                .message(message)
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(
            Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}