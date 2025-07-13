package com.finalYearProject.product.exception;

import com.finalYearProject.product.utilities.exception.FashionException;

public class InvalidUsernameException extends FashionException implements IErrorCode {

    public InvalidUsernameException() {
        super(INVALID_USERNAME_EXCEPTION, null, null);
    }

}