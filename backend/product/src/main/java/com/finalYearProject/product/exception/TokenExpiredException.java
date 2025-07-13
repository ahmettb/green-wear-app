package com.finalYearProject.product.exception;

import com.finalYearProject.product.utilities.exception.FashionException;
import org.springframework.http.HttpStatus;

public class TokenExpiredException extends FashionException implements IErrorCode {

    public TokenExpiredException() {
        super(INVALID_REFRESH_TOKEN, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}