package com.springboot.starter.starterdemo.db.repository;

import com.springboot.starter.starterdemo.db.entity.Quiz;
import com.springboot.starter.starterdemo.db.entity.ScoreCard;
import com.springboot.starter.starterdemo.db.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {

    List<ScoreCard> findByUser(UserAccount userAccount);

    int countByUserAndQuiz(UserAccount userAccount, Quiz quiz);

}
