package com.epam.esm.exception;

public class CertificateBadRequestException extends ResourceBadRequestException {

  public CertificateBadRequestException(String message, Long certificateId) {
    super(message);
    setCertificateId(certificateId);
  }
}
