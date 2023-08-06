package com.akinnova.EventPlannerApi.dto.participantDto;

import lombok.Data;

@Data
public class ParticipantCreationDto {

    private String eventName;

    private String eventId;

    private String participantFirstName;

    private String participantLastName;

    private String participantRole;

    private String email;

    private String phoneNumber;
}
