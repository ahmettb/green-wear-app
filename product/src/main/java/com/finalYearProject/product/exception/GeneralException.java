package com.finalYearProject.product.exception;

import com.finalYearProject.product.utilities.exception.FashionException;
import org.springframework.http.HttpStatus;


public class GeneralException extends FashionException implements IErrorCode {

    public GeneralException(String message) {
        super(GENERAL_EXCEPTION, null, null, new String[]{"message"}, new String[]{message}, HttpStatus.BAD_REQUEST);
    }

}