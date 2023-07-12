package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Boolean existsByParticipantId(String participantId);
    Boolean existsByEventId(String eventId);
    Optional<Participant> findByParticipantId(String participantId);
    Optional<List<Participant>> findByEventId(String eventId);
    Optional<Participant> findByEmail(String email);
    Optional<Participant> findByPhoneNumber(String phoneNumber);
}
