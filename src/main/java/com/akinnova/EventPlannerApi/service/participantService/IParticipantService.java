package com.akinnova.EventPlannerApi.service.participantService;

import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantCreationDto;
import com.akinnova.EventPlannerApi.dto.participantDto.ParticipantUpdateDto;
import com.akinnova.EventPlannerApi.entity.Participant;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IParticipantService {
    ResponsePojo<Participant> registerParticipant(ParticipantCreationDto participantDto);
    ResponsePojo<List<Participant>> findAllParticipants();
    ResponsePojo<Participant> findByParticipantId(String participantId);

    ResponseEntity<?> updateParticipant(ParticipantUpdateDto participantUpdateDto);
    ResponseEntity<?> deleteParticipant(String participantId);
}
