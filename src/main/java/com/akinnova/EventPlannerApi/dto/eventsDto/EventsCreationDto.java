package com.akinnova.EventPlannerApi.dto.eventsDto;

import lombok.Data;


@Data
public class EventsCreationDto {

    private String eventName;

    private String description;

    private String organizerId;

    private String address;

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;
}
