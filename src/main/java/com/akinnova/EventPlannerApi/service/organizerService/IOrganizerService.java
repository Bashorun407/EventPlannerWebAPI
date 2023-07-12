package com.akinnova.EventPlannerApi.service.organizerService;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerCreationDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerUpdateDto;
import com.akinnova.EventPlannerApi.entity.Organizer;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOrganizerService {
    ResponsePojo<Organizer> createOrganizer(OrganizerCreationDto organizerDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponsePojo<List<Organizer>> findAllOrganizer();
    ResponsePojo<Organizer> findByUsername(String username);
    ResponsePojo<Organizer> findByOrganizerId(String organizerId);
    ResponseEntity<?> updateOrganizer(OrganizerUpdateDto organizerUpdateDto);
    ResponseEntity<?> deleteOrganizer(String organizerId);
}
