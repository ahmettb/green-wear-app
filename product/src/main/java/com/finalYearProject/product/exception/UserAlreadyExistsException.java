package com.finalYearProject.product.exception;

import com.finalYearProject.product.utilities.exception.FashionException;

public class UserAlreadyExistsException extends FashionException implements IErrorCode {

    public UserAlreadyExistsException() {
        super(USER_ALREADY_EXISTS_EXCEPTION, null, null, null, null);
    }

}