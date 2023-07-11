package com.akinnova.EventPlannerApi.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDto {
    public String email;
    public String message;
    public LocalDateTime sentDate;
}
