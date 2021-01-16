package com.epam.esm.exception;

public class TagBadRequestException extends ResourceBadRequestException {

  public TagBadRequestException(String message, Long tagId) {
    super(message);
    setTagId(tagId);
  }
}
