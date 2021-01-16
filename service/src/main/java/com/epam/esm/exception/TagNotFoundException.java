package com.epam.esm.exception;

public class TagNotFoundException extends RuntimeException {
  private Long tagId;

  public Long getTagId() {
    return tagId;
  }

  public TagNotFoundException(String message, Long tagId) {
    super(message);
    this.tagId = tagId;
  }
}
