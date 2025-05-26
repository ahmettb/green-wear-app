package com.finalYearProject.product.utilities.advice;

import com.finalYearProject.product.utilities.component.ExceptionUtil;

import com.finalYearProject.product.utilities.exception.FashionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class AuthExceptionAdvice {

    private final ExceptionUtil exceptionUtil;

    @Value("${spring.application.name}")
    private String applicationName="FASHION";

    public AuthExceptionAdvice(final ExceptionUtil exceptionUtil) {
        this.exceptionUtil = exceptionUtil;
    }
//
//    @ExceptionHandler(FashionException.class)
//    public ResponseEntity handleException(FashionException exception, HttpServletRequest request) {
//        return new ResponseEntity<>(exceptionUtil.convert(exception, getLanguageFormRequestHeader(request), applicationName), exception.getHttpStatus());
//    }
//
//    @ExceptionHandler(FashionException.class)
//    public ResponseEntity handleException(FashionException fashionException, HttpServletRequest request) {
//        return new ResponseEntity<>(exceptionUtil.convert(fashionException, getLanguageFormRequestHeader(request), applicationName), HttpStatus.UNPROCESSABLE_ENTITY);
//    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<String> handleRestClientException(HttpStatusCodeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                //.headers(ex.getResponseHeaders())
                .body(new String(ex.getResponseBodyAsByteArray(), StandardCharsets.UTF_8));
    }

    @ExceptionHandler(NestedRuntimeException.class)
    public ResponseEntity handleException(NestedRuntimeException exception, HttpServletRequest request) throws Throwable {
        Throwable root = NestedExceptionUtils.getRootCause(exception);
        if (root instanceof FashionException) {
            return new ResponseEntity<>(exceptionUtil.convert((FashionException) root, getLanguageFormRequestHeader(request), applicationName), ((FashionException) root).getHttpStatus());

        }
        if (root == null) {
            throw exception;
        }
        throw root;
    }

    private String getLanguageFormRequestHeader(HttpServletRequest request) {
        Enumeration<String> languageRanges = request.getHeaders("Accept-Language");
        if (languageRanges.hasMoreElements()) {
            return languageRanges.nextElement();
        }
        return null;
    }
}