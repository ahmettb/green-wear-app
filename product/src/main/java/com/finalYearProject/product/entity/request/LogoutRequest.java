package com.finalYearProject.product.entity.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogoutRequest {

    private String token;

    private String refreshToken;

}