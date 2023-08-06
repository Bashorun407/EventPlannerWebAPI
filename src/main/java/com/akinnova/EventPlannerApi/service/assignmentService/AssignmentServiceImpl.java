package com.akinnova.EventPlannerApi.service.assignmentService;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentCreationDto;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentDeleteDto;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentResponseDto;
import com.akinnova.EventPlannerApi.entity.Assignment;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.AssignmentRepository;

import com.akinnova.EventPlannerApi.repository.LoggedInRepository;
import com.akinnova.EventPlannerApi.repository.OrganizerRepository;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentServiceImpl implements IAssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final OrganizerRepository organizerRepository;
    private final LoggedInRepository loggedInRepository;

    //Class Constructor
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, OrganizerRepository organizerRepository,
                                 LoggedInRepository loggedInRepository) {
        this.assignmentRepository = assignmentRepository;
        this.organizerRepository = organizerRepository;
        this.loggedInRepository = loggedInRepository;
    }

    //This method is called in the Participant's Service class through dependency injection
    @Override
    public ResponsePojo<AssignmentResponseDto> createAssignment(AssignmentCreationDto assignmentDto) {

        Assignment assignment = Assignment.builder()
                .taskName(assignmentDto.getTaskName())
                .description(assignmentDto.getDescription())
                .firstName(assignmentDto.getFirstName())
                .lastName(assignmentDto.getLastName())
                .email(assignmentDto.getEmail())
                .phoneNumber(assignmentDto.getPhoneNumber())
                .taskStatus("uncompleted")
                .build();

        //Saved created assignment to database
        Assignment savedAssignment = assignmentRepository.save(assignment);

        //Response entity
        AssignmentResponseDto assignmentResponseDto = AssignmentResponseDto.builder()
                .taskName(savedAssignment.getTaskName())
                .firstName(savedAssignment.getFirstName())
                .lastName(savedAssignment.getLastName())
                .taskStatus(savedAssignment.getTaskStatus())
                .build();

        ResponsePojo<AssignmentResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format(ResponseUtils.CREATED_MESSAGE, savedAssignment.getLastName()));
        responsePojo.setData(assignmentResponseDto);

        return responsePojo;
    }

    //This method can be called by organizers to check all participants that are assigned tasks
    @Override
    public ResponseEntity<List<AssignmentResponseDto>> findAllAssignments(int pageNum, int pageSize) {
        List<Assignment> assignmentList = assignmentRepository.findAll();
        List<AssignmentResponseDto> responseDtoList = new ArrayList<>(); //empty list for response

//        //Getting responseDto from each assignment object
//        for(Assignment assignment : assignmentList){
//            responseDtoList.add(
//                    AssignmentResponseDto.builder()
//                            .firstName(assignment.getFirstName())
//                            .lastName(assignment.getLastName())
//                            .taskName(assignment.getTaskName())
//                            .taskStatus(assignment.getTaskStatus())
//                            .build()
//            );
//        }

        //Writing the function using Lambda expression
        assignmentList.stream().map(
                assignment -> AssignmentResponseDto.builder()
                        .firstName(assignment.getFirstName())
                        .lastName(assignment.getLastName())
                        .taskName(assignment.getTaskName())
                        .taskStatus(assignment.getTaskStatus())
                        .build()
        ).forEach(responseDtoList::add);

        return ResponseEntity.ok().header("Assignment Page No: ", String.valueOf(pageNum))
                .header("Assignment Page Size: ", String.valueOf(pageSize))
                .header("Assignment Count: ", String.valueOf(assignmentList.size()))
                .body(responseDtoList);
    }

    //This method finds Participants that have been assigned tasks by email
    @Override
    public ResponseEntity<AssignmentResponseDto> findAssignmentByEmail(String email) {
        Assignment assignment = assignmentRepository.findByEmail(email)
                .orElseThrow(()-> new ApiException("Assignee with input email does not exist."));

        AssignmentResponseDto responseDto = AssignmentResponseDto.builder()
                .firstName(assignment.getFirstName())
                .lastName(assignment.getLastName())
                .taskName(assignment.getTaskName())
                .taskStatus(assignment.getTaskStatus())
                .build();


        return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    //This method finds Participants by EventId
    @Override
    public ResponseEntity<List<AssignmentResponseDto>> findAssignmentByEventId(String eventId, int pageNum, int pageSize) {

        List<Assignment> assignmentList = assignmentRepository.findByEventId(eventId)
                .orElseThrow(()-> new ApiException("Assignee with input email not found."));

        List<AssignmentResponseDto> responseDtoList = new ArrayList<>();

        //Using Lambda expression to return response
        assignmentList.stream().map(
                assignment -> AssignmentResponseDto.builder()
                        .firstName(assignment.getFirstName())
                        .lastName(assignment.getLastName())
                        .taskName(assignment.getTaskName())
                        .taskStatus(assignment.getTaskStatus())
                        .build()
        ).forEach(responseDtoList::add);


        return new ResponseEntity<>(responseDtoList, HttpStatus.FOUND);
    }

    //This method finds Participants that have been assigned tasks by phone number
    @Override
    public ResponseEntity<AssignmentResponseDto> findAssignmentByPhoneNumber(String phoneNumber) {
        Assignment assignment = assignmentRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new ApiException("Assignee with input email not found."));

        AssignmentResponseDto responseDto = AssignmentResponseDto.builder()
                .firstName(assignment.getFirstName())
                .lastName(assignment.getLastName())
                .taskName(assignment.getTaskName())
                .taskStatus(assignment.getTaskStatus())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.FOUND);
    }

    //Here Organizer can update Participant's assignment/task
    @Override
    public ResponseEntity<?> updateAssignment(AssignmentCreationDto assignmentDto) {

        Assignment assignmentToUpdate = assignmentRepository.findByPhoneNumber(assignmentDto.getPhoneNumber())
                .orElseThrow(()-> new ApiException("There is no assignee with this phone number: " + assignmentDto.getPhoneNumber()));

        assignmentToUpdate.setTaskName(assignmentDto.getTaskName());
        assignmentToUpdate.setDescription(assignmentDto.getDescription());
        assignmentToUpdate.setFirstName(assignmentDto.getFirstName());
        assignmentToUpdate.setLastName(assignmentDto.getLastName());
        assignmentToUpdate.setEmail(assignmentDto.getEmail());
        assignmentToUpdate.setPhoneNumber(assignmentDto.getPhoneNumber());
        assignmentToUpdate.setTaskStatus(assignmentDto.getTaskStatus());

        //Save update to database
        assignmentRepository.save(assignmentToUpdate);
        return new ResponseEntity<>("Assignee details updated successfully", HttpStatus.OK);
    }

    //This method removes Participants from the Assignment database: determined by the Organizer
    @Override
    public ResponseEntity<?> deleteAssignment(AssignmentDeleteDto assignmentDeleteDto) {

        //Checks if Organizer id exists in Organizer repository
        if(!organizerRepository.existsByOrganizerId(assignmentDeleteDto.getOrganizerId())){
            return new ResponseEntity<>("Organizer with Organizer ID: " + assignmentDeleteDto.getOrganizerId()
                    + " does not exist", HttpStatus.NOT_FOUND);
        }

        if(!loggedInRepository.existsByUsername(assignmentDeleteDto.getUsername())){
            return new ResponseEntity<>("Organizer with username: " + assignmentDeleteDto.getUsername()
                    + " is not logged in.", HttpStatus.NOT_FOUND);
        }

        Assignment assignment = assignmentRepository
                .findByPhoneNumber(assignmentDeleteDto.getPhoneNumber())
                .orElseThrow(()-> new ApiException("Assignee with the phone number: " + assignmentDeleteDto.getPhoneNumber()
                + " does not exist"));

        assignmentRepository.delete(assignment);

        return new ResponseEntity<>("Assignee deleted successfully", HttpStatus.OK);
    }

}
