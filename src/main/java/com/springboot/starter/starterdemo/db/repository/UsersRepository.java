package com.springboot.starter.starterdemo.db.repository;

import com.springboot.starter.starterdemo.db.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);

}
