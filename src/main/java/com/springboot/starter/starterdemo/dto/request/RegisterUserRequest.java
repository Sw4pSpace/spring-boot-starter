package com.springboot.starter.starterdemo.dto.request;

import lombok.Data;

@Data
public class RegisterUserRequest {

    private String username;

    private String password;

    private String confirmPassword;


}
