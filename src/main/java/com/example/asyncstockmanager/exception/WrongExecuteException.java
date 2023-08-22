package com.example.asyncstockmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WrongExecuteException extends RuntimeException {


    public WrongExecuteException(String message, String... resourcePath) {
        super(message);
    }

}
