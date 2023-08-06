package com.akinnova.EventPlannerApi.dto.eventsDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventResponseDto {
    private String eventName;
    private String address;
    private String eventId;
}
