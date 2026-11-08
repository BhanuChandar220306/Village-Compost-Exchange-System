package com.village.compost.controller;

import com.village.compost.dto.response.ApiResponse;
import com.village.compost.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse> myNotifications() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("My notifications")
                        .data(notificationService.getMyNotifications())
                        .build()
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse> unreadCount() {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Unread notification count")
                        .data(notificationService.getUnreadCount())
                        .build()
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markAsRead(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Notification marked as read")
                        .data(notificationService.markAsRead(id))
                        .build()
        );
    }
}