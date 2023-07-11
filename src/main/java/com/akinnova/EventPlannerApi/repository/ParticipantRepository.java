package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
