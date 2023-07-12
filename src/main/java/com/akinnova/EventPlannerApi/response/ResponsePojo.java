package com.akinnova.EventPlannerApi.response;

import lombok.Data;
@Data
public class ResponsePojo <T>{
    int StatusCode = 200;
    boolean success = true;
    String message;
    T data;
}
