package com.akinnova.EventPlannerApi.service.participantService;

import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantCreationDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantUpdateDto;
import com.akinnova.EventPlannerApi.entity.Assignment;
import com.akinnova.EventPlannerApi.entity.Participant;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.AssignmentRepository;
import com.akinnova.EventPlannerApi.repository.EventsRepository;
import com.akinnova.EventPlannerApi.repository.ParticipantRepository;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.response.ResponseUtils;

import com.akinnova.EventPlannerApi.service.participantService.IParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ParticipantServiceImpl implements IParticipantService {
    private final EventsRepository eventsRepository;
    private final ParticipantRepository participantRepository;
    private final AssignmentRepository assignmentRepository;

    //Class Constructor
    public ParticipantServiceImpl(EventsRepository eventsRepository,
                                  ParticipantRepository participantRepository,
                                  AssignmentRepository assignmentRepository) {

        this.eventsRepository = eventsRepository;
        this.participantRepository = participantRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public ResponsePojo<Participant> registerParticipant(ParticipantCreationDto participantDto) {

        //Check if event being registered for exists in Events repository
        if(!eventsRepository.existsByEventId(participantDto.getEventId()))
        {
            throw new ApiException(String.format("Event with id: %s does not exist.", participantDto.getEventId()));
        }

        Participant participant = Participant.builder()
                .participantFirstName(participantDto.getParticipantFirstName())
                .participantLastName(participantDto.getParticipantLastName())
                .participantRole(participantDto.getParticipantRole())
                .participantId(ResponseUtils.generateUniqueIdentifier(5, participantDto.getParticipantLastName()))
                .eventName(participantDto.getEventName())
                .eventId(participantDto.getEventId())
                .email(participantDto.getEmail())
                .phoneNumber(participantDto.getPhoneNumber())
                .rsvpStatus(participantDto.getRsvpStatus())
                .build();

        //Save new participant to repository
        Participant participantToReturn = participantRepository.save(participant);

        //Create new assignment/tasks for every participant that has participant role contained
        if(StringUtils.hasText(participantDto.getParticipantRole())){

            Assignment assignment = Assignment.builder()
                    .taskName(participantDto.getParticipantRole())
                    .eventId(participantDto.getEventId())
                    .firstName(participantDto.getParticipantFirstName())
                    .lastName(participantDto.getParticipantLastName())
                    .description("Participant will carry out task assigned.")
                    .email(participantDto.getEmail())
                    .phoneNumber(participantDto.getPhoneNumber())
                    .taskStatus("In-progress")
                    .build();

            //Save to assignment repository
            assignmentRepository.save(assignment);
        }


        ResponsePojo<Participant> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(participantDto.getParticipantLastName() + ", " + participantDto.getParticipantFirstName()
        + " has been registered successully");

        responsePojo.setData(participantToReturn);


        return responsePojo;
    }

    @Override
    public ResponsePojo<List<Participant>> findAllParticipants() {

        List<Participant> participantList = participantRepository.findAll();

        ResponsePojo<List<Participant>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("List of all Participants");
        responsePojo.setData(participantList);

        return responsePojo;
    }

    @Override
    public ResponsePojo<Participant> findByParticipantId(String participantId) {

        //Checks if participant Id exists in the database
        if(!participantRepository.existsByParticipantId(participantId)){
            throw new ApiException(String.format("Participant with id: %s does not exist.", participantId));
        }

        Participant participant = participantRepository.findByParticipantId(participantId).get();

        ResponsePojo<Participant> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format(ResponseUtils.FOUND_MESSAGE, participant.getParticipantFirstName()));
        responsePojo.setData(participant);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> updateParticipant(ParticipantUpdateDto participantUpdateDto) {

        //Checks if participant Id exists in the database
        if(!participantRepository.existsByParticipantId(participantUpdateDto.getParticipantId())){
            return new ResponseEntity<>("Participant with this id: " + participantUpdateDto.getParticipantId()
                    + " does not exist.", HttpStatus.NOT_FOUND);
        }

        Participant participant = participantRepository.findByParticipantId(participantUpdateDto.getParticipantId()).get();

        participant.setEventName(participantUpdateDto.getEventName());
        participant.setEventId(participantUpdateDto.getEventId());
        participant.setEmail(participantUpdateDto.getEmail());
        participant.setParticipantRole(participantUpdateDto.getParticipantRole());
        participant.setRsvpStatus(participantUpdateDto.getRsvpStatus());

        //Save update in the participant database
        participantRepository.save(participant);

        return new ResponseEntity<>("Participant update was successfully done.", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> deleteParticipant(String participantId) {
        //Checks if participant Id exists in the database
        if(!participantRepository.existsByParticipantId(participantId)){
            return new ResponseEntity<>("Participant with this id: " + participantId
                    + " does not exist.", HttpStatus.NOT_FOUND);
        }

        Participant participantToDelete = participantRepository.findByParticipantId(participantId).get();

        //Deletes participant's detail from the database
        participantRepository.delete(participantToDelete);

        return new ResponseEntity<>("Participant with id: " + participantId + " has been deleted successfully",
                HttpStatus.OK);
    }
}
