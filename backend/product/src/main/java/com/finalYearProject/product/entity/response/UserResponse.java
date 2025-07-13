package com.finalYearProject.product.entity.response;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponse {
    private Long id;


    private String name;

    private String surname;

    private String username;

    private String password;

  //  private Set<Role> roleSet;
}
