package com.finalYearProject.product.exception;


import com.finalYearProject.product.utilities.exception.FashionException;

public class InvalidCodeException extends FashionException implements IErrorCode {

    public InvalidCodeException() {
        super(INVALID_CODE_EXCEPTION, null, null, null, null);
    }
}
