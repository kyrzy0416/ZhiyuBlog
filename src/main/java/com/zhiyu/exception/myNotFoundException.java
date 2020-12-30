package com.zhiyu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class myNotFoundException extends RuntimeException {

    public myNotFoundException() {
    }

    public myNotFoundException(String message) {
        super(message);
    }

    public myNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
