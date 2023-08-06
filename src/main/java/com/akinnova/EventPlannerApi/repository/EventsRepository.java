package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
//    Boolean existsByEventName(String eventName);
    Boolean existsByEventId(String eventId);
    Optional<Events> findByEventId(String eventId);
    Optional<List<Events>> findByEventName(String eventName);
    Optional<List<Events>> findByOrganizerId(String organizerId);

}
