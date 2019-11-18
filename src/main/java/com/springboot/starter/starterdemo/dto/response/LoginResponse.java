package com.springboot.starter.starterdemo.dto.response;

import com.springboot.starter.starterdemo.db.entity.UserAccount;
import lombok.Data;

@Data
public class LoginResponse {

    private boolean successful;
    private String message;
    private UserAccount user;

}
