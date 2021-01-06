package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

  private final CertificateService certificateService;

  public CertificateController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  @GetMapping("/{id}")
  public Certificate readCertificate(@PathVariable long id) {
    return certificateService.read(id);
  }

  @GetMapping
  public List<Certificate> readCertificates() {
    return certificateService.readAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Certificate createCertificate(@RequestBody Certificate certificate) {
    return certificateService.create(certificate);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Certificate updateCertificate(
      @PathVariable long id, @RequestBody Certificate certificate) {
    return certificateService.update(id, certificate);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable long id) {
    certificateService.delete(id);
  }
}
