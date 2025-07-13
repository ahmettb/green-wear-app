package com.finalYearProject.product.entity.request;

import lombok.Data;

import java.util.List;
@Data
public class QuestionRequest {
    private String questionText;

    /** Bu sorunun şıkları */
    private List<OptionRequest> options;

    /** Bu sorunun puanı */
    private Integer point;
}
