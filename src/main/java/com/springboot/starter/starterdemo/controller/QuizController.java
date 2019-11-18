package com.springboot.starter.starterdemo.controller;

import com.springboot.starter.starterdemo.db.entity.Quiz;
import com.springboot.starter.starterdemo.db.entity.ScoreCard;
import com.springboot.starter.starterdemo.db.entity.UserAccount;
import com.springboot.starter.starterdemo.db.repository.QuizQuestionRepository;
import com.springboot.starter.starterdemo.db.repository.QuizRepository;
import com.springboot.starter.starterdemo.db.repository.ScoreCardRepository;
import com.springboot.starter.starterdemo.db.repository.UsersRepository;
import com.springboot.starter.starterdemo.dto.request.SubmitQuizRequest;
import com.springboot.starter.starterdemo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    private ScoreCardRepository scoreCardRepository;
    private UsersRepository usersRepository;
    private QuizRepository quizRepository;
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    public QuizController(ScoreCardRepository scoreCardRepository, UsersRepository usersRepository,
                          QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository) {
        this.scoreCardRepository = scoreCardRepository;
        this.usersRepository = usersRepository;
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        // TODO some checks to verify the quiz is valid
        quiz.getQuestions().forEach(question -> {
            question.setQuiz(quiz);
            question.getAnswers().forEach(answer -> answer.setQuizQuestion(question));
        });
        return this.quizRepository.save(quiz);
    }

    @PostMapping("submit")
    public ScoreCard submitQuiz(@RequestBody SubmitQuizRequest request) {

        if(!this.usersRepository.existsById(request.getUserId())) {
            throw new NotFoundException("User with id \'" + request.getUserId() + "\' does not exist");
        }

        if(!this.quizRepository.existsById(request.getQuizId())) {
            throw new NotFoundException("Quiz with id \'" + request.getQuizId() + "\' does not exist");
        }

        UserAccount userAccount = this.usersRepository.getOne(request.getUserId());
        Quiz quiz = this.quizRepository.getOne(request.getQuizId());

        int numCorrect = 0;
        int total = quiz.getQuestions().size();

        for (int i = 0; i <= (total - 1); i++) {
            if(quiz.getQuestions().get(i).getCorrectAnswer().equalsIgnoreCase(request.getAnswers().get(i))) {
                numCorrect++;
            }
        }

        double score = ((100D/total) * numCorrect);
        ScoreCard scoreCard = new ScoreCard();

        scoreCard.setDateTaken(new Date());
        scoreCard.setScore(score);
        scoreCard.setUser(userAccount);
        scoreCard.setQuiz(quiz);
        return this.scoreCardRepository.save(scoreCard);
    }

    @GetMapping
    public List<Quiz> listQuizzes() {
        return this.quizRepository.findAll();
    }

    @GetMapping("{id}")
    public Quiz fetchQuiz(@PathVariable long id) {
        return this.quizRepository.findById(id).orElse(null);
    }

    @GetMapping("scores/{id}")
    public List<ScoreCard> getScores(@PathVariable("id")long id) {

        if(!this.usersRepository.existsById(id)) {
            throw new NotFoundException("User with id \'" + id + "\' does not exist");
        }

        UserAccount userAccount = this.usersRepository.getOne(id);
        return this.scoreCardRepository.findByUser(userAccount);
    }

}
