package com.akinnova.EventPlannerApi.controller.participantController;

import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantCreationDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantUpdateDto;
import com.akinnova.EventPlannerApi.entity.Participant;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.participantService.ParticipantServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventplanner/auth/participant")
public class ParticipantController {

    @Autowired
    private ParticipantServiceImpl participantService;

    @PostMapping("/register")
    public ResponsePojo<Participant> registerParticipant(@RequestBody ParticipantCreationDto participantDto) {
        return participantService.registerParticipant(participantDto);
    }

    @GetMapping("/participants")
    public ResponsePojo<List<Participant>> findAllParticipants() {
        return participantService.findAllParticipants();
    }

    @GetMapping("/participant/{participantId}")
    public ResponsePojo<Participant> findByParticipantId(@PathVariable String participantId) {
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
