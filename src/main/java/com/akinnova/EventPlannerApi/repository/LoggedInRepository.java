package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.LoggedInUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoggedInRepository extends JpaRepository<LoggedInUsers, Long> {
    Boolean existsByUsername(String username);
    Optional<LoggedInUsers> findByUsername(String username);

}
