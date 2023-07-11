package com.akinnova.EventPlannerApi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventsDto {

    private String eventName;
    private String description;
    private String venue;
    private String address;
    private LocalDateTime startDate;
}
