package com.akinnova.EventPlannerApi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "participant", uniqueConstraints = {
        @UniqueConstraint(columnNames = "participantId"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phoneNumber")
})

public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageAddress;

    private String participantId;

    private String eventName;

    private String eventId;

    private String participantFirstName;

    private String participantLastName;

    private String participantRole;

    private String email;

    private String phoneNumber;


    @OneToMany
    @JoinTable(name = "participant_event",
            joinColumns = @JoinColumn(name = "participant_id", referencedColumnName = "participantId"),
            inverseJoinColumns = @JoinColumn(name = "event", referencedColumnName = "eventName")
    )
    private List<Events> events;
}

