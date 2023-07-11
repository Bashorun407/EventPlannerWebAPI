package com.akinnova.EventPlannerApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizerDto {
    public String firstName;

    public String lastName;

    public String email;

    public String phoneNumber;

    public String password;

    public String role;E
}
