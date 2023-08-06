package com.akinnova.EventPlannerApi.dto.organizerDto;

import lombok.Data;

@Data
public class OrganizerUpdateDto {

    private String organizerId;

    private String imageAddress;

    private String username;

    private String email;

    private String phoneNumber;

    private String password;

    private String role;
}
