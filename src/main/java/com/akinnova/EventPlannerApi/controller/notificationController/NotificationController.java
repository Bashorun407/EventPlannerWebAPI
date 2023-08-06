package com.akinnova.EventPlannerApi.controller.notificationController;

import com.akinnova.EventPlannerApi.dto.notificationDto.NotificationCreationDto;
import com.akinnova.EventPlannerApi.dto.notificationDto.NotificationUpdateDto;
import com.akinnova.EventPlannerApi.entity.Notification;
import com.akinnova.EventPlannerApi.service.notificationService.NotificationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventPlanner/auth/notification")
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationService;

    @PostMapping("/notification")
    public ResponseEntity<?> createNotification(@RequestBody NotificationCreationDto notificationDto) {
        return notificationService.createNotification(notificationDto);
    }

    @GetMapping("/notification/{eventId}")
    public ResponseEntity<Notification> findNotificationByEventId(@PathVariable String eventId) {
        return notificationService.findNotificationByEventId(eventId);
    }

    @PostMapping("/notify/{eventId}")
    public ResponseEntity<?> sendNotification(@PathVariable String eventId) {
        return notificationService.sendNotification(eventId);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateNotification(@RequestBody NotificationUpdateDto notificationUpdateDto) {
        return notificationService.updateNotification(notificationUpdateDto);
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<?> deleteNotification(@PathVariable String eventId) {
        return notificationService.deleteNotification(eventId);
    }
}
