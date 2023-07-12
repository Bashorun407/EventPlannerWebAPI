package com.akinnova.EventPlannerApi.service.notificationService;

import com.akinnova.EventPlannerApi.dto.notificationDto.NotificationCreationDto;
import com.akinnova.EventPlannerApi.dto.notificationDto.NotificationUpdateDto;
import com.akinnova.EventPlannerApi.entity.Notification;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface INotificationService {
    ResponseEntity<?> createNotification(NotificationCreationDto notificationDto);

    ResponsePojo<Notification> findByEventId(String notificationId);
    ResponseEntity<?> sendNotification(String eventId);
    ResponseEntity<?> updateNotification(NotificationUpdateDto notificationUpdateDto);
    ResponseEntity<?> deleteNotification(String eventId);
}
