package com.akinnova.EventPlannerApi.service.assignmentService;

import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentDto;
import com.akinnova.EventPlannerApi.entity.Assignment;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAssignmentService {

    ResponsePojo<Assignment> createAssignment(AssignmentDto assignmentDto);
    ResponsePojo<List<Assignment>> findAllAssignments();
    ResponsePojo<Assignment> findAssignmentByEmail(String email);
    ResponsePojo<Assignment> findAssignmentByPhoneNumber(String phoneNumber);
    ResponseEntity<?> updateAssignment(AssignmentDto assignmentDto);
    ResponseEntity<?> deleteAssignment(String phoneNumber);
}
