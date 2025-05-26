package com.finalYearProject.product.controller.advice;

import com.finalYearProject.product.utilities.advice.AuthExceptionAdvice;
import com.finalYearProject.product.utilities.component.ExceptionUtil;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice extends AuthExceptionAdvice {

    public ControllerExceptionAdvice(final ExceptionUtil exceptionUtil) {
        super(exceptionUtil);
    }

}