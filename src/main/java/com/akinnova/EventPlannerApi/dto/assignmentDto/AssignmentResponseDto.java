package com.akinnova.EventPlannerApi.dto.assignmentDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignmentResponseDto {
    private String taskName;
    private String firstName;
    private String lastName;
    private String taskStatus;
}
