package com.akinnova.EventPlannerApi.controller.eventsController;

import com.akinnova.EventPlannerApi.dto.eventsDto.EventResponseDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsCreationDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsUpdateDto;

import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.eventService.EventsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventPlanner/auth/events")
public class EventsController {

    @Autowired
    private EventsServiceImpl eventsService;

    @PostMapping("/event")
    public ResponsePojo<EventResponseDto> createEvent(@RequestBody EventsCreationDto eventsDto) {
        return eventsService.createEvent(eventsDto);
    }

    @GetMapping("/allEvents")
    public ResponseEntity<List<EventResponseDto>> findAllEvents(@RequestParam(defaultValue = "1") int pageNum,
                                                                @RequestParam(defaultValue = "20") int pageSize) {
        return eventsService.findAllEvents(pageNum, pageSize);
    }

    @GetMapping("/eventByName/{eventName}")
    public ResponseEntity<List<EventResponseDto>> findEventByEventName(@PathVariable String eventName,
                                                     @RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return eventsService.findEventByEventName(eventName, pageNum, pageSize);
    }

    @PutMapping("/event")
    public ResponseEntity<?> updateEvent(@RequestBody EventsUpdateDto eventsUpdateDto) {
        return eventsService.updateEvent(eventsUpdateDto);
    }

    @DeleteMapping("/event/{eventName}")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventName) {
        return eventsService.deleteEvent(eventName);
    }

    @GetMapping("/eventSearch")
    ResponseEntity<?> searchAll(@RequestParam String eventName, @RequestParam String eventId,
                                @RequestParam String organizerId, @RequestParam int pageNum, @RequestParam int pageSize){
        return eventsService.searchAll(eventName, eventId, organizerId, pageNum, pageSize);
    }
}
