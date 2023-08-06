package com.akinnova.EventPlannerApi.service.participantService;

import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantCreationDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantResponseDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantUpdateDto;
import com.akinnova.EventPlannerApi.entity.Assignment;
import com.akinnova.EventPlannerApi.entity.Participant;
import com.akinnova.EventPlannerApi.entity.Roles;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.AssignmentRepository;
import com.akinnova.EventPlannerApi.repository.EventsRepository;
import com.akinnova.EventPlannerApi.repository.ParticipantRepository;
import com.akinnova.EventPlannerApi.repository.RolesRepository;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.response.ResponseUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipantServiceImpl implements IParticipantService {
    private final EventsRepository eventsRepository;
    private final ParticipantRepository participantRepository;
    private final AssignmentRepository assignmentRepository;
    private final RolesRepository rolesRepository;

    //Class Constructor
    public ParticipantServiceImpl(EventsRepository eventsRepository,
                                  ParticipantRepository participantRepository,
                                  AssignmentRepository assignmentRepository, RolesRepository rolesRepository) {

        this.eventsRepository = eventsRepository;
        this.participantRepository = participantRepository;
        this.assignmentRepository = assignmentRepository;
        this.rolesRepository = rolesRepository;
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

            Roles roles = Roles.builder()
                    .roleName(participantDto.getParticipantRole())
                    .emailAddress(participantDto.getEmail())
                    .phoneNumber(participantDto.getPhoneNumber())
                    .build();

            //Save to Roles Repository
            rolesRepository.save(roles);
        }


        ResponsePojo<Participant> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(participantDto.getParticipantLastName() + ", " + participantDto.getParticipantFirstName()
        + " has been added successfully");

        responsePojo.setData(participantToReturn);

        return responsePojo;
    }

    @Override
    public ResponseEntity<List<ParticipantResponseDto>> findAllParticipants(int pageNum, int pageSize) {

        List<Participant> participantList = participantRepository.findAll();
        List<ParticipantResponseDto> responseDtoList = new ArrayList<>();

        participantList.stream().skip(pageNum - 1).limit(pageSize).map(
                participant -> ParticipantResponseDto.builder()
                        .eventName(participant.getEventName())
                        .participantId(participant.getParticipantId())
                        .participantRole(participant.getParticipantRole())
                        .build()
        ).forEach(responseDtoList::add);

        return ResponseEntity.ok()
                .header("Participants Page No: ", String.valueOf(pageNum))
                .header("Participants Page Size: ", String.valueOf(pageSize))
                .header("Participants Count: ", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<ParticipantResponseDto> findByParticipantId(String participantId) {

        Participant participant = participantRepository.findByParticipantId(participantId)
                .orElseThrow(()->
                        new ApiException(String.format("Participant with id: %s does not exist.", participantId)));


        return ResponseEntity.ok(ParticipantResponseDto.builder()
                .eventName(participant.getEventName())
                .participantId(participant.getParticipantId())
                .participantRole(participant.getParticipantRole())
                .build()
        );
    }

    @Override
    public ResponseEntity<?> updateParticipant(ParticipantUpdateDto participantUpdateDto) {

        Participant participant = participantRepository.findByParticipantId(participantUpdateDto.getParticipantId())
                .orElseThrow(()-> new ApiException("Participant with this id: " + participantUpdateDto.getParticipantId()
                        + " does not exist."));

        participant.setEventName(participantUpdateDto.getEventName());
        participant.setEventId(participantUpdateDto.getEventId());
        participant.setEmail(participantUpdateDto.getEmail());
        participant.setParticipantRole(participantUpdateDto.getParticipantRole());

        //Save update in the participant database
        participantRepository.save(participant);

        return new ResponseEntity<>("Participant update was successfully done.", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> deleteParticipant(String participantId) {

        Participant participantToDelete = participantRepository.findByParticipantId(participantId)
                .orElseThrow(()-> new ApiException("Participant with this id: " + participantId
                        + " does not exist."));

        //Deletes participant's detail from the database
        participantRepository.delete(participantToDelete);

        return new ResponseEntity<>("Participant with id: " + participantId + " has been deleted successfully",
                HttpStatus.OK);
    }
}
