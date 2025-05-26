package com.finalYearProject.product.exception;

import com.finalYearProject.product.utilities.exception.FashionException;

public class BadCredentialsException extends FashionException implements IErrorCode {

    public BadCredentialsException() {
        super(BAD_CREDENTIALS_EXCEPTION, null, null, null, null);
    }

}