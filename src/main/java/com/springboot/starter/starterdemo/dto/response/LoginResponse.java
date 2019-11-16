package com.springboot.starter.starterdemo.dto.response;

import lombok.Data;

@Data
public class LoginResponse {

    private boolean successful;
    private String message;

}
