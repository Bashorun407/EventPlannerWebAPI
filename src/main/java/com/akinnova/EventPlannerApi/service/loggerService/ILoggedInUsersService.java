package com.akinnova.EventPlannerApi.service.loggerService;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import com.akinnova.EventPlannerApi.entity.LoggedInUsers;
import com.akinnova.EventPlannerApi.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ILoggedInUsersService {

    ResponseEntity<?> userLogIn(LoginDto loginDto);
    ResponsePojo<List<LoggedInUsers>> findAllLoggedInUsers();
    ResponsePojo<LoggedInUsers> findLoggedInUserByUsername(String username);
    ResponseEntity<?> logOutUser(String username);
}
