package com.akinnova.EventPlannerApi.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "login_table",
        uniqueConstraints = @UniqueConstraint(columnNames = "username")
)
public class LoggedInUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;
}
