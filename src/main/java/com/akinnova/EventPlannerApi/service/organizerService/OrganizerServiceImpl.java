package com.akinnova.EventPlannerApi.service.organizerService;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerCreationDto;
import com.akinnova.EventPlannerApi.dto.organizerDto.OrganizerUpdateDto;
import com.akinnova.EventPlannerApi.email.EmailDetail;
import com.akinnova.EventPlannerApi.email.EmailService;
import com.akinnova.EventPlannerApi.entity.Organizer;
import com.akinnova.EventPlannerApi.entity.Roles;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.LoggedInRepository;
import com.akinnova.EventPlannerApi.repository.OrganizerRepository;
import com.akinnova.EventPlannerApi.repository.RolesRepository;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.response.ResponseUtils;

import com.akinnova.EventPlannerApi.service.loggerService.LoggedInUsersImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizerServiceImpl implements IOrganizerService {
    private final EmailService emailService;
    private final RolesRepository rolesRepository;
    private final OrganizerRepository organizerRepository;
    private final LoggedInRepository loggedInRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final LoggedInUsersImpl loggedInUsers;

    //Class Constructor


    public OrganizerServiceImpl(EmailService emailService, RolesRepository rolesRepository,
                                OrganizerRepository organizerRepository, LoggedInRepository loggedInRepository,
                                AuthenticationManager authenticationManager,
                                PasswordEncoder passwordEncoder, LoggedInUsersImpl loggedInUsers) {
        this.emailService = emailService;
        this.rolesRepository = rolesRepository;
        this.organizerRepository = organizerRepository;
        this.loggedInRepository = loggedInRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.loggedInUsers = loggedInUsers;
    }

    @Override
    public ResponsePojo<Organizer> createOrganizer(OrganizerCreationDto organizerDto) {
        Organizer organizer = Organizer.builder()
                .organizerId(ResponseUtils.generateUniqueIdentifier(5, organizerDto.getUsername()))
                .firstName(organizerDto.getFirstName())
                .lastName(organizerDto.getLastName())
                .username(organizerDto.getUsername())
                .email(organizerDto.getEmail())
                .phoneNumber(organizerDto.getPhoneNumber())
                .password(passwordEncoder.encode(organizerDto.getPassword()))
                .role(organizerDto.getRole())
                .build();

        //Save to Organizer repository
       Organizer organizerToReturn = organizerRepository.save(organizer);

       EmailDetail emailDetail = EmailDetail.builder()
                .subject("ConvenerApp Accounts Creation")
                .body("Good day " + organizerDto.getLastName() +", " + organizerDto.getFirstName()
                        + "\n You have successfully created an account with ConvenerApp . "
                        + "\n Your account details are: \n"
                        + "Username: " + organizerDto.getUsername() + "\n"
                        + "Password: " + organizerDto.getPassword() + "\n"
                        + "Organizer id: " + organizerToReturn.getOrganizerId() + "\n"
                        + "\n\n Thank you.")
                .recipient(organizerDto.getEmail())
                .build();

        //Sends email to each participant
        emailService.sendSimpleEmail(emailDetail);

        Roles roles = Roles.builder()
                .roleName(organizer.getRole())
                .build();

        //Save to Roles repository
        rolesRepository.save(roles);

        ResponsePojo<Organizer> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Organizer created successfully");
        responsePojo.setData(organizerToReturn);
        return responsePojo;
    }

    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Organizers that are logged in are automatically added into the loggedIn repository
        loggedInUsers.userLogIn(loginDto);

        return new ResponseEntity<>("User " + loginDto.getUsername() + " logged in successfully", HttpStatus.OK);
    }

    @Override
    public ResponsePojo<List<Organizer>> findAllOrganizer() {

        List<Organizer> organizerList = organizerRepository.findAll();

        ResponsePojo<List<Organizer>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("All organizers: ");
        responsePojo.setData(organizerList);
        return responsePojo;
    }

    @Override
    public ResponsePojo<Organizer> findByUsername(String username) {
        Optional<Organizer> organizerOptional = organizerRepository.findByUsername(username);

        organizerOptional.orElseThrow(()-> new ApiException(String.format("Organizer with username: %s does not exist.", username)));

        Organizer organizer = organizerOptional.get();

        ResponsePojo<Organizer> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Organizer with username: " + username + " found.");
        responsePojo.setData(organizer);

        return responsePojo;
    }

    @Override
    public ResponsePojo<Organizer> findByOrganizerId(String organizerId) {

        if(!organizerRepository.existsByOrganizerId(organizerId)){
            throw new ApiException(String.format("Organizer with id: %s does not exist", organizerId));
        }

        Organizer organizer = organizerRepository.findByOrganizerId(organizerId).get();

        ResponsePojo<Organizer> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format(ResponseUtils.FOUND_MESSAGE, organizer.getUsername()));
        responsePojo.setData(organizer);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> updateOrganizer(OrganizerUpdateDto organizerUpdateDto) {
        //Checks if an organizer exists in the Organizer database
        if(!organizerRepository.existsByOrganizerId(organizerUpdateDto.getOrganizerId())){
            return new ResponseEntity<>("Organizer with id: " + organizerUpdateDto.getOrganizerId()
                    + " does not exist.",
                    HttpStatus.NOT_FOUND);
        }

        //Checks if Organizer is logged in
        if(!loggedInRepository.existsByUsername(organizerUpdateDto.getUsername())){
            return new ResponseEntity<>("Organizer with username: " + organizerUpdateDto.getUsername()
                    + "is not logged in", HttpStatus.FORBIDDEN);
        }

        Organizer organizerToUpdate = organizerRepository.findByOrganizerId(organizerUpdateDto.getOrganizerId()).get();

        organizerToUpdate.setUsername(organizerUpdateDto.getUsername());
        organizerToUpdate.setEmail(organizerUpdateDto.getEmail());
        organizerToUpdate.setPhoneNumber(organizerUpdateDto.getPhoneNumber());
        organizerToUpdate.setPassword(passwordEncoder.encode(organizerUpdateDto.getPassword()));
        organizerToUpdate.setRole(organizerUpdateDto.getRole());

        //Save changes to repository
        organizerRepository.save(organizerToUpdate);

        return new ResponseEntity<>("Organizer updated successfully", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> deleteOrganizer(String organizerId) {
        //Checks if the organizer exists in the Organizer database
        if(!organizerRepository.existsByOrganizerId(organizerId)){
            return new ResponseEntity<>("Organizer with id: " + organizerId + " does not exist.", HttpStatus.NOT_FOUND);
        }

        Organizer organizer = organizerRepository.findByOrganizerId(organizerId).get();
        organizerRepository.delete(organizer);

        return new ResponseEntity<>("Organizer id: " + organizerId + " deleted successfully.", HttpStatus.GONE);
    }
}
