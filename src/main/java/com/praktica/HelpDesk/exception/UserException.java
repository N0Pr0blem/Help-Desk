package com.praktica.HelpDesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserException extends ApiException {
    public UserException(String message, String errorCode) {
        super(message, errorCode);
    }
}
