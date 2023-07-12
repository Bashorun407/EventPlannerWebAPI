package com.akinnova.EventPlannerApi.service.assignmentService;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentDto;
import com.akinnova.EventPlannerApi.entity.Assignment;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.AssignmentRepository;
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

    //Class Constructor
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public ResponsePojo<Assignment> createAssignment(AssignmentDto assignmentDto) {

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

    @Override
    public ResponsePojo<List<Assignment>> findAllAssignments() {

        List<Assignment> assignmentList = assignmentRepository.findAll();

        ResponsePojo<List<Assignment>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("All assignments: ");
        responsePojo.setData(assignmentList);
        return responsePojo;
    }

    @Override
    public ResponsePojo<Assignment> findAssignmentByEmail(String email) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findByEmail(email);
        assignmentOptional.orElseThrow(()-> new ApiException("Assignment with input email not found."));

        ResponsePojo<Assignment> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Assignment found by Email: ");
        responsePojo.setData(assignmentOptional.get());

        return responsePojo;
    }

    @Override
    public ResponsePojo<Assignment> findAssignmentByPhoneNumber(String phoneNumber) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findByPhoneNumber(phoneNumber);
        assignmentOptional.orElseThrow(()-> new ApiException("Assignment with input email not found."));

        ResponsePojo<Assignment> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Assignment found by Email: ");
        responsePojo.setData(assignmentOptional.get());

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> updateAssignment(AssignmentDto assignmentDto) {
        if(!assignmentRepository.existsByPhoneNumber(assignmentDto.getPhoneNumber()))
            return new ResponseEntity<>("Assignment with the phone number does not exist", HttpStatus.BAD_REQUEST);

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
        return new ResponseEntity<>("Assignment details updated successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteAssignment(String phoneNumber) {

        if(!assignmentRepository.existsByPhoneNumber(phoneNumber))
            return new ResponseEntity<>("Assignment with the phone number does not exist", HttpStatus.BAD_REQUEST);

        Optional<Assignment> assignmentOptional = assignmentRepository.findByPhoneNumber(phoneNumber);
        Assignment assignmentToDelete = assignmentOptional.get();

        assignmentRepository.delete(assignmentToDelete);

        return new ResponseEntity<>("Assignment deleted successfully", HttpStatus.OK);
    }
}
