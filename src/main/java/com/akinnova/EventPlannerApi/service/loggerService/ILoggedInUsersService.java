package com.akinnova.EventPlannerApi.service.loggerService;

import com.akinnova.EventPlannerApi.dto.logInDto.LoginDto;
import org.springframework.http.ResponseEntity;

public interface ILoggedInUsersService {

    ResponseEntity<?> userLogIn(LoginDto loginDto);
    ResponseEntity<?> findAllLoggedInUsers(int pageNum, int pageSize);
    ResponseEntity<?> findLoggedInUserByUsername(String username);
    ResponseEntity<?> logOutUser(String username);
}
