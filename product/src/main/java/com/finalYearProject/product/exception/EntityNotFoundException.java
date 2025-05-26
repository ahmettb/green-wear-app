package com.finalYearProject.product.exception;

import com.finalYearProject.product.utilities.exception.FashionException;

public class EntityNotFoundException extends FashionException implements IErrorCode {

    public EntityNotFoundException(String id, Class clazz) {
        super(ENTITY_NOT_FOUND_EXCEPTION, null, new String[]{id, clazz.getSimpleName()});
    }

    public EntityNotFoundException(Long id, Class clazz) {
        super(ENTITY_NOT_FOUND_EXCEPTION, null, new String[]{id.toString(), clazz.getSimpleName()});
    }

}