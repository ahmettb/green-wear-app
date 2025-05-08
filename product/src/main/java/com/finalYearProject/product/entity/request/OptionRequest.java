package com.finalYearProject.product.entity.request;

import lombok.Data;

@Data
public class OptionRequest
{
    private String text;

    /** Doğru cevap mı? */
    private Boolean correct;
}
