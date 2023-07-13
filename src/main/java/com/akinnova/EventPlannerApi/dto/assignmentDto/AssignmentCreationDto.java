package com.akinnova.EventPlannerApi.dto.assignmentDto;

import lombok.Data;

@Data
public class AssignmentCreationDto {
    private String taskName;

    private String eventId;

    private String description;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String taskStatus;
}
