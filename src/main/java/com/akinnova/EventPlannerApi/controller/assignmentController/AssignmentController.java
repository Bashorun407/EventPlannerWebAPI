package com.akinnova.EventPlannerApi.controller.assignmentController;

import com.akinnova.EventPlannerApi.dto.assignmentDto.AssignmentDto;
import com.akinnova.EventPlannerApi.entity.Assignment;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.assignmentService.AssignmentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventplanner/auth/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentServiceImpl assignmentService;

    //This method was implemented in Participant entity class
    @GetMapping("/assignment")
    public ResponsePojo<Assignment> createAssignment(@RequestBody AssignmentDto assignmentDto) {
        return assignmentService.createAssignment(assignmentDto);
    }

    //Method to fetch all assignments
    @GetMapping("/assignments")
    public ResponsePojo<List<Assignment>> findAllAssignments() {
        return assignmentService.findAllAssignments();
    }

    //Method to fetch assignment by email
    @GetMapping("/assignmentE/{email}")
    public ResponsePojo<Assignment> findAssignmentByEmail(@PathVariable String email) {
        return assignmentService.findAssignmentByEmail(email);
    }

    //Method to fetch assignment by phone number
    @GetMapping("/assignmentP/{phoneNumber}")
    public ResponsePojo<Assignment> findAssignmentByPhoneNumber(@PathVariable String phoneNumber) {
        return assignmentService.findAssignmentByPhoneNumber(phoneNumber);
    }

    //Method to update assignment
    @PutMapping("/update")
    public ResponseEntity<?> updateAssignment(@RequestBody AssignmentDto assignmentDto) {
        return assignmentService.updateAssignment(assignmentDto);
    }

    //Method to delete assignment
    @DeleteMapping("/delete/{phoneNumber}")
    public ResponseEntity<?> deleteAssignment(@PathVariable String phoneNumber) {
        return assignmentService.deleteAssignment(phoneNumber);
    }
}
