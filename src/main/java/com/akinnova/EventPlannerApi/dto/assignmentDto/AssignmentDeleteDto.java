package com.akinnova.EventPlannerApi.dto.assignmentDto;

import lombok.Data;

@Data
public class AssignmentDeleteDto {
    private String username;
    private String organizerId;
    private String eventId;
    private String phoneNumber;
}
