package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
