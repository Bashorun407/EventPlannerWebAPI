package com.akinnova.EventPlannerApi.controller.loggerController;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.entity.LoggedInUsers;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import com.akinnova.EventPlannerApi.service.loggerService.LoggedInUsersImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/eventplanner/auth/login")
public class LoggedInUserController {

    @Autowired
    private LoggedInUsersImpl loggedInUsers;

    //Log in is initiated in Organizer Controller
    @PostMapping("/userLogin")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto loginDto) {
        return loggedInUsers.userLogIn(loginDto);
    }

    //Method to return details of all logged in users
    @GetMapping("/loggedInUsers")
    public ResponsePojo<List<LoggedInUsers>> findAllLoggedInUsers() {
        return loggedInUsers.findAllLoggedInUsers();
    }

    @GetMapping("/loggedInUser/{username}")
    public ResponsePojo<LoggedInUsers> findLoggedInUserByUsername(@PathVariable String username) {
        return loggedInUsers.findLoggedInUserByUsername(username);
    }

    @DeleteMapping("/logOutUser/{username}")
    public ResponseEntity<?> logOutUser(@PathVariable String username) {
        return loggedInUsers.logOutUser(username);
    }
}
