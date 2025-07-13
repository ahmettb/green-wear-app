package com.finalYearProject.product.exception;

public interface IErrorCode {
    // Sistem hataları 9000 , uygulama hataları 1000 ile baslar
    int ENTITY_NOT_FOUND_EXCEPTION = 9100;

    int BAD_CREDENTIALS_EXCEPTION = 1000;
    int REQUIRED_FIELDS_CANNOT_BE_EMPTY_EXCEPTION = 1001;
    int INVALID_REFRESH_TOKEN = 1002;
    int USER_ALREADY_EXISTS_EXCEPTION = 1003;
    int GENERAL_EXCEPTION = 1004;
    int INVALID_USERNAME_EXCEPTION = 1005;
    int INVALID_CODE_EXCEPTION = 1006;

}