package com.praktica.HelpDesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessException extends ApiException {
    public AccessException() {
        super("Permission denied","YOU_SHELL_NOT_PASS");
    }
}
