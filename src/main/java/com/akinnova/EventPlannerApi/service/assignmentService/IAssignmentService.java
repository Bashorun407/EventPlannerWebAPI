package com.akinnova.EventPlannerApi.service.assignmentService;

import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentCreationDto;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentDeleteDto;
import com.akinnova.EventPlannerApi.entity.Assignment;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAssignmentService {

    ResponsePojo<Assignment> createAssignment(AssignmentCreationDto assignmentDto);
    ResponsePojo<List<Assignment>> findAllAssignments();
    ResponsePojo<Assignment> findAssignmentByEmail(String email);
    ResponsePojo<Assignment> findAssignmentByEventId(String eventId);
    ResponsePojo<Assignment> findAssignmentByPhoneNumber(String phoneNumber);
    ResponseEntity<?> updateAssignment(AssignmentCreationDto assignmentDto);
    ResponseEntity<?> deleteAssignment(AssignmentDeleteDto assignmentDeleteDto);
    ResponseEntity<?> autoDeleteAssignment();
}
