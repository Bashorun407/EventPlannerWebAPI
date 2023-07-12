package com.akinnova.EventPlannerApi.dto.participantDto;

import lombok.Data;

@Data
public class ParticipantUpdateDto {

    private String participantId;

    private String eventName;

    private String eventId;

    private String email;

    private String phoneNumber;

    private String participantRole;

    private String rsvpStatus;

}
