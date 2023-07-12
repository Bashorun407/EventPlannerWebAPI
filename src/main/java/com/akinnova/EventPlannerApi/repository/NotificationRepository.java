package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Boolean existsByEventId(String eventId);
    Optional<Notification> findByEventId(String notificationId);
}
