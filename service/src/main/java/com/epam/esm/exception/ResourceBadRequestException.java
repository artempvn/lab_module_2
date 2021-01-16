package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ResourceBadRequestException extends RuntimeException {
  private Long tagId;
  private Long certificateId;

  public Long getTagId() {
    return tagId;
  }

  public Long getCertificateId() {
    return certificateId;
  }

  public void setTagId(Long tagId) {
    this.tagId = tagId;
  }

  public void setCertificateId(Long certificateId) {
    this.certificateId = certificateId;
  }

  public ResourceBadRequestException(String message) {
    super(message);
  }

  public ResourceBadRequestException(String message, Long tagId, Long certificateId) {
    super(message);
    this.tagId = tagId;
    this.certificateId = certificateId;
  }
}
