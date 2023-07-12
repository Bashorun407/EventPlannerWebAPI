package com.akinnova.EventPlannerApi.service.loggerService;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.entity.LoggedInUsers;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.LoggedInRepository;
import com.akinnova.EventPlannerApi.repository.OrganizerRepository;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.loggerService.ILoggedInUsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoggedInUsersImpl implements ILoggedInUsersService {

    private final LoggedInRepository loggedInRepository;
    private final OrganizerRepository organizerRepository;

    //Class constructor
    public LoggedInUsersImpl(LoggedInRepository loggedInRepository, OrganizerRepository organizerRepository) {
        this.loggedInRepository = loggedInRepository;
        this.organizerRepository = organizerRepository;
    }

    @Override
    public ResponseEntity<?> userLogIn(LoginDto loginDto) {

        //Check if Organizer username exists in the Organizer repository
        if(!organizerRepository.existsByUsername(loginDto.getUsername())){
            throw new ApiException("Organizer with username: " + loginDto.getUsername() + " does not exist");
        }

        LoggedInUsers loggedInUsers = LoggedInUsers.builder()
                .username(loginDto.getUsername())
                .password(loginDto.getPassword())
                .build();

        //saves user's login details to database
        loggedInRepository.save(loggedInUsers);
        return new ResponseEntity<>("Organizer has been logged in", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponsePojo<List<LoggedInUsers>> findAllLoggedInUsers() {
        List<LoggedInUsers> loggedInUsersList = loggedInRepository.findAll();

        ResponsePojo<List<LoggedInUsers>> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("List of Logged in users");
        responsePojo.setData(loggedInUsersList);

        return responsePojo;
    }

    @Override
    public ResponsePojo<LoggedInUsers> findLoggedInUserByUsername(String username) {
        Optional<LoggedInUsers> loggedInUsersOptional = loggedInRepository.findByUsername(username);

        loggedInUsersOptional.orElseThrow(
                ()-> new ApiException(String.format("User with username: %s not logged in", username)));
        ResponsePojo<LoggedInUsers> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("User log in details");
        responsePojo.setData(loggedInUsersOptional.get());

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> logOutUser(String username) {

        //Check if username exists in Organizer database
        if(!organizerRepository.existsByUsername(username)){
            return new ResponseEntity<>("User with username: " + username + " does not exist.", HttpStatus.NOT_FOUND);
        }

        //Check if username exists in logged repository
        if(!loggedInRepository.existsByUsername(username)){
            return new ResponseEntity<>("User with username: " + username + " is not logged in.", HttpStatus.NO_CONTENT);
        }

        //Now find user by username
        LoggedInUsers loggedInUser = loggedInRepository.findByUsername(username).get();

        //Remove user from database
        loggedInRepository.delete(loggedInUser);

        return new ResponseEntity<>(username + " logged out successfully", HttpStatus.GONE);
    }
}
