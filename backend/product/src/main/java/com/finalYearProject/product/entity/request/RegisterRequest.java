package com.finalYearProject.product.entity.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String email;

    private String password;

    private String name ;

    private String surname;
}
