package com.akinnova.EventPlannerApi.dto.organizerDto;

import lombok.Data;

@Data
public class OrganizerCreationDto {
    private String imageAddress;
    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String phoneNumber;

    private String password;

    private String role;
}
