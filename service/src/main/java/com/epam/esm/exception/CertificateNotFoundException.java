package com.epam.esm.exception;

public class CertificateNotFoundException extends RuntimeException {

  private final Long certificateId;

  public Long getCertificateId() {
    return certificateId;
  }

  public CertificateNotFoundException(String message, Long certificateId) {
    super(message);
    this.certificateId = certificateId;
  }
}
