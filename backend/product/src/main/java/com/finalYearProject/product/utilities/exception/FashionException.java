package com.finalYearProject.product.utilities.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FashionException extends RuntimeException {

    private Integer code;
    private String message;
    private String description;
    private String[] fields;
    private String[] placeHolders;
    private HttpStatus httpStatus;

    public FashionException(int code, String message, String description, String[] fields, String[] placeHolders, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.message = message;
        this.description = description;
        this.fields = fields;
        this.placeHolders = placeHolders;
        this.httpStatus = httpStatus;
    }

    public FashionException(int code, String message, String description, String[] fields, String[] placeHolders) {
        this(code, message, description, fields, placeHolders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public FashionException(int code, String[] fields, String[] placeHolders) {
        this(code, null, null, fields, placeHolders, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public FashionException(int code, String[] fields) {
        this(code, null, null, fields, null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public FashionException(int code) {
        this(code, null, null, null, null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public FashionException(int code, HttpStatus httpStatus) {
        this(code, null, null, null, null, httpStatus);
    }


}