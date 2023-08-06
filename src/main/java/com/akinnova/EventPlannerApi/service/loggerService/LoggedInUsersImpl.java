package com.akinnova.EventPlannerApi.service.loggerService;

import com.akinnova.EventPlannerApi.dto.logInDto.LogResponseDto;
import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.entity.LoggedInUsers;
import com.akinnova.EventPlannerApi.exception.ApiException;
import com.akinnova.EventPlannerApi.repository.LoggedInRepository;
import com.akinnova.EventPlannerApi.repository.OrganizerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<List<LogResponseDto>> findAllLoggedInUsers(int pageNum, int pageSize) {
        List<LoggedInUsers> loggedInUsersList = loggedInRepository.findAll();
        List<LogResponseDto> responseDtoList = new ArrayList<>();

        loggedInUsersList.stream().map(
                loggedInUsers -> LogResponseDto.builder()
                        .username(loggedInUsers.getUsername())
                        .build()
        ).forEach(responseDtoList::add);

        return ResponseEntity.ok()
                .header("Log in Page Number: ", String.valueOf(pageNum))
                .header("Log in Page Size: ", String.valueOf(pageSize))
                .header("Number of logged in Users: ", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<LogResponseDto> findLoggedInUserByUsername(String username) {
        LoggedInUsers loggedInUsers = loggedInRepository.findByUsername(username)
                .orElseThrow(()->
                        new ApiException(String.format("User with username: %s not logged in", username)));
       LogResponseDto logResponseDto = LogResponseDto.builder()
               .username(loggedInUsers.getUsername())
               .build();

        return ResponseEntity.ok(logResponseDto);
    }

    @Override
    public ResponseEntity<?> logOutUser(String username) {

        //Now find user by username
        LoggedInUsers loggedInUser = loggedInRepository.findByUsername(username)
                .orElseThrow(()-> new ApiException("User by username: " + username + ", is not logged in."));

        //Remove user from database
        loggedInRepository.delete(loggedInUser);

        return new ResponseEntity<>(username + " logged out successfully", HttpStatus.GONE);
    }
}
