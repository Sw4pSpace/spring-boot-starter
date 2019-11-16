package com.springboot.starter.starterdemo.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

}
