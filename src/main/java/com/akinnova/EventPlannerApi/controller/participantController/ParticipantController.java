package com.akinnova.EventPlannerApi.controller.participantController;

import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantCreationDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantResponseDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantUpdateDto;
import com.akinnova.EventPlannerApi.entity.Participant;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.participantService.ParticipantServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventPlanner/auth/participant")
public class ParticipantController {

    @Autowired
    private ParticipantServiceImpl participantService;

    @PostMapping("/register")
    public ResponsePojo<Participant> registerParticipant(@RequestBody ParticipantCreationDto participantDto) {
        return participantService.registerParticipant(participantDto);
    }

    @GetMapping("/participants")
    public ResponseEntity<List<ParticipantResponseDto>> findAllParticipants(@RequestParam(defaultValue = "1") int pageNum,
                                                                            @RequestParam(defaultValue = "20") int pageSize) {
        return participantService.findAllParticipants(pageNum, pageSize);
    }

    @GetMapping("/participant/{participantId}")
    public ResponseEntity<ParticipantResponseDto> findByParticipantId(@PathVariable String participantId) {
        return participantService.findByParticipantId(participantId);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateParticipant(@RequestBody ParticipantUpdateDto participantUpdateDto) {
        return participantService.updateParticipant(participantUpdateDto);
    }

    @DeleteMapping("/delete/{participantId}")
    public ResponseEntity<?> deleteParticipant(@PathVariable String participantId) {
        return participantService.deleteParticipant(participantId);
    }
}
