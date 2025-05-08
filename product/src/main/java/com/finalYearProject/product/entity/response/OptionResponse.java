package com.finalYearProject.product.entity.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionResponse {

    private String text;
    private boolean correct;
}
