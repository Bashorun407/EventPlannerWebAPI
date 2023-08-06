package com.akinnova.EventPlannerApi.service.organizerService;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerCreationDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerUpdateDto;
import com.akinnova.EventPlannerApi.entity.Organizer;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;


public interface IOrganizerService {
    ResponsePojo<Organizer> createOrganizer(OrganizerCreationDto organizerDto);
    ResponseEntity<?> login(LoginDto loginDto);
    ResponseEntity<?> findAllOrganizer(int pageNum, int pageSize);
    ResponseEntity<?> findByUsername(String username);
    ResponseEntity<?> findByOrganizerId(String organizerId);
    ResponseEntity<?> updateOrganizer(OrganizerUpdateDto organizerUpdateDto);
    ResponseEntity<?> deleteOrganizer(String organizerId);
}
