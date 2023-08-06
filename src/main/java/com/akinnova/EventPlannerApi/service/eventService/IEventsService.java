package com.akinnova.EventPlannerApi.service.eventService;

import com.akinnova.EventPlannerApi.dto.eventsDto.EventResponseDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsCreationDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsUpdateDto;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;


public interface IEventsService {
    ResponsePojo<EventResponseDto> createEvent(EventsCreationDto eventsDto);
    ResponseEntity<?> findAllEvents(int pageNum, int pageSize);
    ResponseEntity<?> findEventByOrganizerId(String organizerId, int pageNum, int pageSize);
    ResponseEntity<?> findEventByEventId(String eventId);
    ResponseEntity<?> findEventByEventName(String eventName, int pageNum, int pageSize);
    ResponseEntity<?> updateEvent(EventsUpdateDto eventsUpdateDto);
    ResponseEntity<?> autoUpdate();
    ResponseEntity<?> deleteEvent(String eventId);

    ResponseEntity<?> searchAll(String eventName, String eventId, String organizerId, int pageNum, int pageSize);
}
