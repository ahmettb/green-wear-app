package com.finalYearProject.product.utilities.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ErrorWrapperDto {

    private int code;
    private String applicationName;
    private String message;
    private String description;
    private String[] fields;
    private List<ErrorWrapperDto> errors;

    public ErrorWrapperDto(){

    }

    public ErrorWrapperDto(List<ErrorWrapperDto> errors){
        this();
        this.errors = errors;
    }

}