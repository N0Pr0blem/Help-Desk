package com.praktica.HelpDesk.exception;

public class TaskException extends ApiException {
  public TaskException(String message, String errorCode) {
    super(message, errorCode);
  }
}
