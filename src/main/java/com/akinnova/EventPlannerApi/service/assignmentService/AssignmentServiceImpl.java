package com.akinnova.EventPlannerApi.service.assignmentService;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentCreationDto;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentDeleteDto;
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

import java.util.List;
import java.util.Optional;

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
    public ResponsePojo<Assignment> createAssignment(AssignmentCreationDto assignmentDto) {

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

        ResponsePojo<Assignment> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format(ResponseUtils.CREATED_MESSAGE, savedAssignment.getLastName()));
        responsePojo.setData(savedAssignment);

        return responsePojo;
    }

    //This method can be called by organizers to check all participants that are assigned tasks
    @Override
    public ResponsePojo<List<Assignment>> findAllAssignments() {
        List<Assignment> assignmentList = assignmentRepository.findAll();

        ResponsePojo<List<Assignment>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("All assignees: ");
        responsePojo.setData(assignmentList);
        return responsePojo;
    }

    //This method finds Participants that have been assigned tasks by email
    @Override
    public ResponsePojo<Assignment> findAssignmentByEmail(String email) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findByEmail(email);
        assignmentOptional.orElseThrow(()-> new ApiException("Assignee with input email not found."));

        ResponsePojo<Assignment> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Assignee found by Email: ");
        responsePojo.setData(assignmentOptional.get());

        return responsePojo;
    }

    //This method finds Participants by EventId
    @Override
    public ResponsePojo<Assignment> findAssignmentByEventId(String eventId) {

        Optional<Assignment> assignmentOptional = assignmentRepository.findByEventId(eventId);
        assignmentOptional.orElseThrow(()-> new ApiException("Assignee with input email not found."));

        ResponsePojo<Assignment> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Assignee found by EventId: ");
        responsePojo.setData(assignmentOptional.get());

        return responsePojo;
    }

    //This method finds Participants that have been assigned tasks by phone number
    @Override
    public ResponsePojo<Assignment> findAssignmentByPhoneNumber(String phoneNumber) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findByPhoneNumber(phoneNumber);
        assignmentOptional.orElseThrow(()-> new ApiException("Assignee with input email not found."));

        ResponsePojo<Assignment> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Assignee found by Email: ");
        responsePojo.setData(assignmentOptional.get());

        return responsePojo;
    }

    //Here Organizer can update Participant's assignment/task
    @Override
    public ResponseEntity<?> updateAssignment(AssignmentCreationDto assignmentDto) {

        //Checks that participant exists by phone number
        if(!assignmentRepository.existsByPhoneNumber(assignmentDto.getPhoneNumber()))
            return new ResponseEntity<>("Assignee with the phone number does not exist", HttpStatus.BAD_REQUEST);

        Optional<Assignment> assignmentOptional = assignmentRepository.findByPhoneNumber(assignmentDto.getPhoneNumber());
        Assignment assignmentToUpdate = assignmentOptional.get();

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

        if(!assignmentRepository.existsByPhoneNumber(assignmentDeleteDto.getPhoneNumber()))
            return new ResponseEntity<>("Assignment with the phone number does not exist",
                    HttpStatus.BAD_REQUEST);

        Optional<Assignment> assignmentOptional = assignmentRepository
                .findByPhoneNumber(assignmentDeleteDto.getPhoneNumber());

        Assignment assignmentToDelete = assignmentOptional.get();

        assignmentRepository.delete(assignmentToDelete);

        return new ResponseEntity<>("Assignee deleted successfully", HttpStatus.OK);
    }

    // TODO: 13/07/2023 Attempt to create an automatic delete method to remove all Assignees when event is over

    //This method attempts to delete automatically

    @Override
    public ResponseEntity<?> autoDeleteAssignment() {
        //Checks Assignment repository for events whose dates are same as the system's date.
        return null;
    }


}
