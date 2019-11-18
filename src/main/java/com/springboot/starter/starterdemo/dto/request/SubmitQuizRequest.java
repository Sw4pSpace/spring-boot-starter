package com.springboot.starter.starterdemo.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SubmitQuizRequest {

    private long userId;

    private long quizId;

    private List<String> answers;

}
