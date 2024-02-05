package com.oa.homework.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String s) {
        super(s);
    }
}
