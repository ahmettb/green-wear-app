package com.finalYearProject.product.entity.request;


import lombok.Data;

@Data
public class SolveQuizRequest {

        private Integer questionNo;

        private Long userId;

        private Long quizId;

        private Long questionId;

        private String answerChoice;


}
