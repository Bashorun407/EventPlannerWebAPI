package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Boolean existsByEmail(String username);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<Assignment> findByTaskName(String taskName);
    Optional<Assignment> findByEmail(String email);
    Optional<Assignment> findByPhoneNumber(String phoneNumber);
    Optional<List<Assignment>> findByTaskStatus(String status);
}
