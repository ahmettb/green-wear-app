package com.finalYearProject.product.entity.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForgetPasswordRequest {


    private String email;

    private String vkn;

}
