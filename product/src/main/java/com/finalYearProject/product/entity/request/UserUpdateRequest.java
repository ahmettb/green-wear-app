package com.finalYearProject.product.entity.request;

import lombok.Data;

@Data
public class UserUpdateRequest {

        private Long userId;

        private String name;

        private String surname;

        private String mail;


}
