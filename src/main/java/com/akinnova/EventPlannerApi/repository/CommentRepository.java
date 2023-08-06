package com.akinnova.EventPlannerApi.repository;

import com.akinnova.EventPlannerApi.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
//    Boolean existsByUsername(String username);
//
    Optional<List<Comments>> findByUsername(String username);
}
