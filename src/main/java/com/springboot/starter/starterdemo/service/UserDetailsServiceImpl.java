package com.springboot.starter.starterdemo.service;

import com.springboot.starter.starterdemo.db.entity.UserAccount;
import com.springboot.starter.starterdemo.db.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UsersRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        UserAccount userAccount = user.get();
        return new User(userAccount.getUsername(), userAccount.getPassword(), emptyList());
    }
}
