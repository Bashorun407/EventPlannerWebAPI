package com.akinnova.EventPlannerApi.dto.notificationDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationCreationDto {

    private String eventName;

    private String eventId;

    private String subject;

    private String mailBody;

    private LocalDateTime sentDate;

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;
}
