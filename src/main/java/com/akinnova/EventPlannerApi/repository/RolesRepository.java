package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
}
