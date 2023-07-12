package com.akinnova.EventPlannerApi.controller.organizerController;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerCreationDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerUpdateDto;
import com.akinnova.EventPlannerApi.entity.Organizer;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.organizerService.OrganizerServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventplanner/auth/organizer")
public class OrganizerController {

    @Autowired
    private OrganizerServiceImpl organizerService;
    @PostMapping("/organizer")
    public ResponsePojo<Organizer> createOrganizer(@RequestBody OrganizerCreationDto organizerDto) {
        return organizerService.createOrganizer(organizerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return organizerService.login(loginDto);
    }
    @GetMapping("/organizers")
    public ResponsePojo<List<Organizer>> findAllOrganizer() {
        return organizerService.findAllOrganizer();
    }

    @GetMapping("/organizerUname/{username}")
    public ResponsePojo<Organizer> findByUsername(@PathVariable String username) {
        return organizerService.findByUsername(username);
    }

    @GetMapping("/organizer/{organizerId}")
    public ResponsePojo<Organizer> findByOrganizerId(@PathVariable String organizerId) {
        return organizerService.findByOrganizerId(organizerId);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrganizer(@RequestBody OrganizerUpdateDto organizerUpdateDto) {
        return organizerService.updateOrganizer(organizerUpdateDto);
    }

    @DeleteMapping("/delete/{organizerId}")
    public ResponseEntity<?> deleteOrganizer(@PathVariable String organizerId) {
        return organizerService.deleteOrganizer(organizerId);
    }
}
