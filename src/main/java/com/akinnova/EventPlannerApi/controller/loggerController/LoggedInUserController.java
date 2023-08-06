package com.akinnova.EventPlannerApi.controller.loggerController;

import com.akinnova.EventPlannerApi.dto.logInDto.LogResponseDto;
import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.service.loggerService.LoggedInUsersImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventPlanner/auth/login")
public class LoggedInUserController {

    @Autowired
    private LoggedInUsersImpl loggedInUsers;

    //Log in is initiated in Organizer Controller
    @PostMapping("/userLogin")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto loginDto) {
        return loggedInUsers.userLogIn(loginDto);
    }

    //Method to return details of all logged-in users
    @GetMapping("/loggedInUsers")
    public ResponseEntity<List<LogResponseDto>> findAllLoggedInUsers(@RequestParam(defaultValue = "1") int pageNum,
                                                                     @RequestParam(defaultValue = "20") int pageSize) {
        return loggedInUsers.findAllLoggedInUsers(pageNum, pageSize);
    }

    @GetMapping("/loggedInUser/{username}")
    public ResponseEntity<LogResponseDto> findLoggedInUserByUsername(@PathVariable String username) {
        return loggedInUsers.findLoggedInUserByUsername(username);
    }

    @DeleteMapping("/logOutUser/{username}")
    public ResponseEntity<?> logOutUser(@PathVariable String username) {
        return loggedInUsers.logOutUser(username);
    }
}
