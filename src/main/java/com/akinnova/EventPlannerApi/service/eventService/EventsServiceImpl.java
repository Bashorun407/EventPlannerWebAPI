package com.akinnova.EventPlannerApi.service.eventService;

import com.akinnova.EventPlannerApi.dto.eventsDto.EventResponseDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsCreationDto;
import com.akinnova.EventPlannerApi.dto.eventsDto.EventsUpdateDto;
import com.akinnova.EventPlannerApi.entity.Events;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.EventsRepository;
import com.akinnova.EventPlannerApi.repository.OrganizerRepository;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.response.ResponseUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventsServiceImpl implements IEventsService {
    private final EventsRepository eventsRepository;
    private final OrganizerRepository organizerRepository;

    //Class constructor
    public EventsServiceImpl(EventsRepository eventsRepository, OrganizerRepository organizerRepository) {
        this.eventsRepository = eventsRepository;
        this.organizerRepository = organizerRepository;
    }

    @Override
    public ResponsePojo<EventResponseDto> createEvent(EventsCreationDto eventsDto) {

        //Check if organizer for event exists
        boolean check = organizerRepository.existsByOrganizerId(eventsDto.getOrganizerId());
        if(!check){
            throw new ApiException(String.format("Organizer with id: %s does not exist", eventsDto.getOrganizerId()));
        }
        Events events = Events.builder()
                .eventName(eventsDto.getEventName())
                .description(eventsDto.getDescription())
                .organizerId(eventsDto.getOrganizerId())
                .eventId(ResponseUtils.generateUniqueIdentifier(5, eventsDto.getEventName()))
                .address(eventsDto.getAddress())
                .eventCompletion(false)
                .startDate(LocalDateTime.of(eventsDto.getYear(),eventsDto.getMonth(), eventsDto.getDay(),
                        eventsDto.getHour(), eventsDto.getMinute()))
                .build();
        Events savedEvent = eventsRepository.save(events);

        EventResponseDto responseDto = EventResponseDto.builder()
                .eventName(savedEvent.getEventName())
                .eventId(savedEvent.getEventId())
                .address(savedEvent.getAddress())
                .build();

        ResponsePojo<EventResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format(String.format(ResponseUtils.CREATED_MESSAGE, responseDto.getEventName()),
                savedEvent.getEventName()));
        responsePojo.setData(responseDto);
        return responsePojo;
    }


    @Override
    public ResponseEntity<List<EventResponseDto>> findAllEvents(int pageNum, int pageSize) {
        List<Events> allEvents = eventsRepository.findAll();
        List<EventResponseDto> responseDtoList = new ArrayList<>();

        allEvents.stream().skip(pageNum - 1).limit(pageSize).map(
                events -> EventResponseDto.builder()
                        .eventName(events.getEventName())
                        .eventId(events.getEventId())
                        .address(events.getAddress())
                        .build()
        ).forEach(responseDtoList::add);

        return ResponseEntity.ok()
                .header("Event Page Number: ", String.valueOf(pageNum))
                .header("Event Page Size: ", String.valueOf(pageSize))
                .header("Events Count: ", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findEventByOrganizerId(String organizerId, int pageNum, int pageSize) {

        List<Events> eventsList = eventsRepository.findByOrganizerId(organizerId)
                .orElseThrow(()->
                new ApiException("There are no events by this organizer id: " + organizerId));

        List<EventResponseDto> responseDtoList = new ArrayList<>();

        eventsList.stream().skip(pageNum - 1).limit(pageSize).map(
                events -> EventResponseDto.builder()
                        .eventName(events.getEventName())
                        .eventId(events.getEventId())
                        .address(events.getAddress())
                        .build()
        ).forEach(responseDtoList::add);


        return ResponseEntity.ok()
                .header("Event Page Number: ", String.valueOf(pageNum))
                .header("Event Page Size: ", String.valueOf(pageSize))
                .header("Events Count: ", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findEventByEventId(String eventId) {

        Events events = eventsRepository.findByEventId(eventId)
                .orElseThrow(()-> new ApiException("There is no event by this id: " + eventId));

        EventResponseDto responseDto = EventResponseDto.builder()
                .eventName(events.getEventName())
                .eventId(events.getEventId())
                .address(events.getAddress())
                .build();


        return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<List<EventResponseDto>> findEventByEventName(String eventName, int pageNum, int pageSize) {

        List<Events> eventsList = eventsRepository.findByEventName(eventName)
                .orElseThrow(()-> new ApiException(String.format("Event-name :%s does not exist", eventName)));

        List<EventResponseDto> responseDtoList = new ArrayList<>();

        eventsList.stream().skip(pageNum - 1).limit(pageSize).map(
                events -> EventResponseDto.builder()
                        .eventName(events.getEventName())
                        .eventId(events.getEventId())
                        .address(events.getAddress())
                        .build()
        ).forEach(responseDtoList::add);

        return ResponseEntity.ok()
                .header("Event Page Number: ", String.valueOf(pageNum))
                .header("Event Page Size: ", String.valueOf(pageSize))
                .header("Events Count: ", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> updateEvent(EventsUpdateDto eventsUpdateDto) {

        Events eventToUpdate = eventsRepository.findByEventId(eventsUpdateDto.getEventId())
                .orElseThrow(()-> new ApiException("Event with name: " + eventsUpdateDto.getEventName()
                        + " entered does not exist."));

        eventToUpdate.setEventName(eventToUpdate.getEventName());
        eventToUpdate.setAddress(eventsUpdateDto.getAddress());
        eventToUpdate.setStartDate(LocalDateTime.of(eventsUpdateDto.getYear(), eventsUpdateDto.getMonth(),
                eventsUpdateDto.getDay(),
                eventsUpdateDto.getHour(),
                eventsUpdateDto.getMinute()));

        //Save update to database
        eventsRepository.save(eventToUpdate);

        return new ResponseEntity<>("Event name: " + eventToUpdate.getEventName() + " has been updated",
                HttpStatus.ACCEPTED);
    }

    //This method auto updates the eventCompletion status once the eventDate tallies with System Date
    @Override
    public ResponseEntity<?> autoUpdate() {
        return null;
    }

    @Override
    public ResponseEntity<?> deleteEvent(String eventId) {

        Events eventToDelete = eventsRepository.findByEventId(eventId)
                .orElseThrow(()-> new ApiException("Event id: " + eventId +  "does not exist."));
        //Delete the event from the database
        eventsRepository.delete(eventToDelete);

        return new ResponseEntity<>("Event with id: " + eventId + " has been deleted.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchAll(String eventName, String eventId, String organizerId, int pageNum, int pageSize) {

        List<Events> searchList = new ArrayList<>();
        List<EventResponseDto> responseDtoList = new ArrayList<>();

        if(StringUtils.hasText(eventName)){
            searchList = eventsRepository.findByEventName(eventName)
                    .orElseThrow(()-> new ApiException("Event by event-name: " + eventName + " not found."));

        }


        if(StringUtils.hasText(eventId)){
            Events events = eventsRepository.findByEventId(eventId).orElseThrow(() ->
                    new ApiException("Event by event-id: " + eventId + " not found."));
            searchList.add(events);
        }

        if(StringUtils.hasText(organizerId)){
            searchList = eventsRepository.findByOrganizerId(organizerId).
                    orElseThrow(()-> new ApiException("Event by organizer-id: " + organizerId + " not found."));
        }

        searchList.stream().skip(pageNum -1).limit(pageSize).map(
                events -> EventResponseDto.builder()
                        .eventName(events.getEventName())
                        .eventId(events.getEventId())
                        .address(events.getAddress())
                        .build()
        ).forEach(responseDtoList::add);

        return ResponseEntity.ok()
                .header("Event Page Number: ", String.valueOf(pageNum))
                .header("Event Page Size: ", String.valueOf(pageSize))
                .header("Events Count: ", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }


}
