package com.finalYearProject.product.entity.response;


import com.finalYearProject.product.constant.RANK;
import com.finalYearProject.product.entity.Quiz;
import lombok.Data;

import java.util.List;

@Data

public class UserDtoResponse {

    private Long userId;
    private String username;
    private String name;
    private String mail;
    private String surname;

    private Double nextRankMinPoint;

    private Double maxPoint;
    private RANK rank;
    private Double point;

    private List<QuizResponse> quizResponse;




}
