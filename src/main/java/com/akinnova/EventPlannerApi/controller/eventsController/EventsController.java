package com.akinnova.EventPlannerApi.controller.eventsController;

import com.akinnova.EventPlannerApi.dto.eventsDto.EventsCreationDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsUpdateDto;
import com.akinnova.EventPlannerApi.entity.Events;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.eventService.EventsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventplanner/auth/events")
public class EventsController {

    @Autowired
    private EventsServiceImpl eventsService;

    @PostMapping("/event")
    public ResponsePojo<Events> createEvent(@RequestBody EventsCreationDto eventsDto) {
        return eventsService.createEvent(eventsDto);
    }

    @GetMapping("/allEvents")
    public ResponsePojo<List<Events>> findAllEvents() {
        return eventsService.findAllEvents();
    }

    @GetMapping("/eventByName/{eventName}")
    public ResponsePojo<Events> findEventByEventName(@PathVariable String eventName) {
        return eventsService.findEventByEventName(eventName);
    }

    @PutMapping("/event")
    public ResponseEntity<?> updateEvent(@RequestBody EventsUpdateDto eventsUpdateDto) {
        return eventsService.updateEvent(eventsUpdateDto);
    }

    @DeleteMapping("/event/{eventName}")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventName) {
        return eventsService.deleteEvent(eventName);
    }
}
