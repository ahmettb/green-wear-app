package com.finalYearProject.product.entity.response;


import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class QuizResponse {
    private List<QuestionResponse> questions;
    private Long userId;
    private String title;
    private String description;
    private Boolean status;
    private Long couponId;
    private Integer minPoint;

}
