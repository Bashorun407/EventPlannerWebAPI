package com.akinnova.EventPlannerApi.service.eventService;

import com.akinnova.EventPlannerApi.dto.eventsDto.EventsCreationDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsUpdateDto;
import com.akinnova.EventPlannerApi.entity.Events;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IEventsService {
    ResponsePojo<Events> createEvent(EventsCreationDto eventsDto);
    ResponsePojo<List<Events>> findAllEvents();
    ResponsePojo<Events> findEventByEventName(String eventName);
    //ResponsePojo<List<Events>> findEventByVenue(String venue);
    ResponseEntity<?> updateEvent(EventsUpdateDto eventsUpdateDto);
    ResponseEntity<?> autoUpdate();
    ResponseEntity<?> deleteEvent(String eventId);


}
