package com.akinnova.EventPlannerApi.dto.eventsDto;

import lombok.Data;

@Data
public class EventsUpdateDto {
    private String eventName;

    private String eventId;

    private String address;

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

}
