package com.epam.esm.entity;

public class Response {

  private String errorMessage;
  private String errorCode;

  public Response(String errorMessage, String errorCode) {
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
