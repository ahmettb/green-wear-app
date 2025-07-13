package com.finalYearProject.product.entity.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RefreshTokenRequest {

    private String token;

    private String refreshToken;

}