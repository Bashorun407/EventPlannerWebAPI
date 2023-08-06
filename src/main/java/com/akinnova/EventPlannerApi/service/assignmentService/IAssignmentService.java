package com.akinnova.EventPlannerApi.service.assignmentService;

import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentCreationDto;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentDeleteDto;
import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentResponseDto;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface IAssignmentService {

    ResponsePojo<AssignmentResponseDto> createAssignment(AssignmentCreationDto assignmentDto);
    ResponseEntity<?> findAllAssignments(int pageNum, int pageSize);
    ResponseEntity<?> findAssignmentByEmail(String email);
    ResponseEntity<?> findAssignmentByEventId(String eventId, int pageNum, int pageSize);
    ResponseEntity<?> findAssignmentByPhoneNumber(String phoneNumber);
    ResponseEntity<?> updateAssignment(AssignmentCreationDto assignmentDto);
    ResponseEntity<?> deleteAssignment(AssignmentDeleteDto assignmentDeleteDto);
}
