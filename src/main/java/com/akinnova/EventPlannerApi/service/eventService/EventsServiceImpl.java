package com.akinnova.EventPlannerApi.service.eventService;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public ResponsePojo<Events> createEvent(EventsCreationDto eventsDto) {

        //Check if organizer for event exists
        if(!organizerRepository.existsByOrganizerId(eventsDto.getOrganizerId())){
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


        ResponsePojo<Events> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format(ResponseUtils.CREATED_MESSAGE, savedEvent.getEventName()));
        responsePojo.setData(savedEvent);
        return responsePojo;
    }


    @Override
    public ResponsePojo<List<Events>> findAllEvents() {
        List<Events> allEvents = eventsRepository.findAll();

        ResponsePojo<List<Events>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("All events: ");
        responsePojo.setData(allEvents);
        return responsePojo;
    }

    @Override
    public ResponsePojo<Events> findEventByEventName(String eventName) {

        Optional<Events> eventsOptional = eventsRepository.findByEventName(eventName);

        eventsOptional.orElseThrow(()-> new ApiException(String.format("Event-name :%s does not exist", eventName)));

        Events eventToReturn = eventsOptional.get();

        ResponsePojo<Events> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format("Event name: %s found", eventName));
        responsePojo.setData(eventToReturn);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> updateEvent(EventsUpdateDto eventsUpdateDto) {
        if(!eventsRepository.existsByEventId(eventsUpdateDto.getEventId())){
            return new ResponseEntity<>("Event with name: " + eventsUpdateDto.getEventName()
                    + " entered does not exist.", HttpStatus.NOT_FOUND);
        }

        Events eventToUpdate = eventsRepository.findByEventId(eventsUpdateDto.getEventId()).get();
        eventToUpdate.setEventName(eventToUpdate.getEventName());
        eventToUpdate.setAddress(eventsUpdateDto.getAddress());
        eventToUpdate.setStartDate(LocalDateTime.of(eventsUpdateDto.getYear(), eventsUpdateDto.getMonth(), eventsUpdateDto.getDay(),
                eventsUpdateDto.getHour(), eventsUpdateDto.getMinute()));

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
        if(!eventsRepository.existsByEventId(eventId)){
            return new ResponseEntity<>("Event id: " + eventId + " does not exist.", HttpStatus.NOT_FOUND);
        }

        Events eventToDelete = eventsRepository.findByEventId(eventId).get();
        //Delete the event from the database
        eventsRepository.delete(eventToDelete);

        return new ResponseEntity<>("Event with id: " + eventId + " has been deleted.", HttpStatus.OK);
    }
}
