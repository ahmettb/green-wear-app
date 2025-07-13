package com.finalYearProject.product.entity.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResetPasswordRequest {

    private String authCode;

    private String newPassword;

}
