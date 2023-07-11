package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventsRepository extends JpaRepository<Events, Long> {
    Optional<Events> findByEventName(String eventName);
}
