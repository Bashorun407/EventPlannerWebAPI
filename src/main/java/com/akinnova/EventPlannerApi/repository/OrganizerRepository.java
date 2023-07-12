package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    Boolean existsByOrganizerId(String organizerId);
    Boolean existsByUsername(String username);
    Optional<Organizer> findByUsername(String username);
    Optional<Organizer> findByOrganizerId(String organizerId);
    Optional<Organizer> findByEmail(String email);
    Optional<Organizer> findByPhoneNumber(String phoneNumber);

}
