package com.akinnova.EventPlannerApi.dto.participantDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantResponseDto {
    private String eventName;

    private String participantId;

    private String participantRole;
}
