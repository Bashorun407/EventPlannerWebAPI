package com.akinnova.EventPlannerApi.dto.assignmentDto;

import lombok.Data;

@Data
public class AssignmentDto {
    private String taskName;

    private String description;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String taskStatus;
}
