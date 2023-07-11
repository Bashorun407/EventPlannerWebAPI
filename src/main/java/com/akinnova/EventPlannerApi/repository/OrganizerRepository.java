package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
}
