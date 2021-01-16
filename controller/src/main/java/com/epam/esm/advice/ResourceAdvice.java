package com.epam.esm.advice;

import com.epam.esm.entity.Response;
import com.epam.esm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceAdvice {

  @ExceptionHandler(TagBadRequestException.class)
  public ResponseEntity<Response> handleException(TagBadRequestException e) {
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getTagId());
    Response response = new Response(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CertificateBadRequestException.class)
  public ResponseEntity<Response> handleException(CertificateBadRequestException e) {
    String errorCode = String.format("%s%s", HttpStatus.BAD_REQUEST.value(), e.getCertificateId());
    Response response = new Response(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ResourceBadRequestException.class)
  public ResponseEntity<Response> handleException(ResourceBadRequestException e) {
    String errorCode =
        String.format(
            "%s:%s.%s", HttpStatus.BAD_REQUEST.value(), e.getTagId(), e.getCertificateId());
    Response response = new Response(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(TagNotFoundException.class)
  public ResponseEntity<Response> handleException(TagNotFoundException e) {
    String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), e.getTagId());
    Response response = new Response(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CertificateNotFoundException.class)
  public ResponseEntity<Response> handleException(CertificateNotFoundException e) {
    String errorCode = String.format("%s%s", HttpStatus.NOT_FOUND.value(), e.getCertificateId());
    Response response = new Response(e.getMessage(), errorCode);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
}
