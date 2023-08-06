package com.akinnova.EventPlannerApi.service.participantService;

import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantCreationDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantUpdateDto;
import com.akinnova.EventPlannerApi.entity.Participant;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface IParticipantService {
    ResponsePojo<Participant> registerParticipant(ParticipantCreationDto participantDto);
    ResponseEntity<?> findAllParticipants(int pageNum, int pageSize);
    ResponseEntity<?> findByParticipantId(String participantId);

    ResponseEntity<?> updateParticipant(ParticipantUpdateDto participantUpdateDto);
    ResponseEntity<?> deleteParticipant(String participantId);
}
