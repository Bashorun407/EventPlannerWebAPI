package com.akinnova.EventPlannerApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignmentDto {
    private String title;
    private String description;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String taskStatus;
    private Boolean activeStatus;
}
