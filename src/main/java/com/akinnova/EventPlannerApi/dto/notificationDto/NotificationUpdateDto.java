package com.akinnova.EventPlannerApi.dto.notificationDto;

import lombok.Data;

@Data
public class NotificationUpdateDto {
    private String eventId;

    private String subject;

    private String mailBody;

    //private LocalDateTime sentDate;

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

}
