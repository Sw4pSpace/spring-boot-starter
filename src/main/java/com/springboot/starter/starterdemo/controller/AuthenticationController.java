package com.springboot.starter.starterdemo.controller;

import com.springboot.starter.starterdemo.db.entity.UserAccount;
import com.springboot.starter.starterdemo.db.repository.UsersRepository;
import com.springboot.starter.starterdemo.dto.request.LoginRequest;
import com.springboot.starter.starterdemo.dto.request.RegisterUserRequest;
import com.springboot.starter.starterdemo.dto.response.LoginResponse;
import com.springboot.starter.starterdemo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
                                    UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("login")
    private LoginResponse login(@RequestBody LoginRequest request) {
        LoginResponse response = new LoginResponse();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(userDetails, request.getPassword(), userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            response.setSuccessful(true);
            response.setMessage("Login successful");
            return response;
        }

        response.setSuccessful(false);
        response.setMessage("Login unsuccessful");
        return response;
    }

    @PostMapping("register")
    private LoginResponse register(@RequestBody RegisterUserRequest request) {

        LoginResponse response = new LoginResponse();
        Optional<UserAccount> userAccount = this.usersRepository.findByUsername(request.getUsername());

        if(userAccount.isPresent()) {
            response.setSuccessful(false);
            response.setMessage("User with supplied username already exists");
            return response;
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            response.setSuccessful(false);
            response.setMessage("Password do not match");
            return response;
        }

        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(this.bCryptPasswordEncoder.encode(request.getPassword()));
        user.setLastLogin(new Date());
        user.setCreatedDate(new Date());
        this.usersRepository.save(user);

        response.setSuccessful(true);
        response.setMessage("User registered successfully");
        return response;
    }

}
