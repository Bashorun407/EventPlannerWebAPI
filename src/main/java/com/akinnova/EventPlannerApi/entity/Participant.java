package com.akinnova.EventPlannerApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String organizerId;

    public String participantFirstName;

    public String ParticipantLastName;
    public String email;

    public String phoneNumber;

    public String RSVPStatus;
}

