package com.akinnova.EventPlannerApi.dto.organizerDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizerResponseDto {
    private String username;
    private String role;
}
