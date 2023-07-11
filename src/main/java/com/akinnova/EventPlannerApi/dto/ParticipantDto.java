package com.akinnova.EventPlannerApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantDto {
    public String organizerId;

    public String participantFirstName;

    public String ParticipantLastName;
    public String email;

    public String phoneNumber;

    public String RSVPStatus;
}
