package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

  private final CertificateService certificateService;

  public CertificateController(CertificateService certificateService) {
    this.certificateService = certificateService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Certificate> readCertificate(@PathVariable long id) {
    Optional<Certificate> certificate = certificateService.read(id);
    return certificate.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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
  public ResponseEntity<Certificate> updateCertificate(
      @PathVariable long id, @RequestBody Certificate certificate) {
    Optional<Certificate> updatedCertificate = certificateService.update(id, certificate);
    return updatedCertificate.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable long id) {
    certificateService.delete(id);
  }
}
