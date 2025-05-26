package com.finalYearProject.product.utilities.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KolayFaturaBulkException extends RuntimeException {

    private List<FashionException> exceptions = new ArrayList<>();

    public void throwIfNecessary() {
        if (!exceptions.isEmpty()) {
            throw this;
        }
    }

    public void addException(FashionException FashionException) {
        this.exceptions.add(FashionException);
    }

}