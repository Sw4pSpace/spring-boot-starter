package com.springboot.starter.starterdemo.db.repository;

import com.springboot.starter.starterdemo.db.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

}
