package com.epam.esm.controller;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.GetParameter;
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
    return certificate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Certificate> readCertificates(
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String description,
      @RequestParam(name = "sort.date", required = false) Boolean sortByDate,
      @RequestParam(name = "sort.name", required = false) Boolean sortByName,
      @RequestParam(name = "sort.asc", required = false) Boolean sortAsc) {
    GetParameter parameter =
        GetParameter.builder()
            .tagName(tag)
            .name(name)
            .description(description)
            .sortByDate(sortByDate)
            .sortByName(sortByName)
            .sortAsc(sortAsc)
            .build();
    return certificateService.readAll(parameter);
  }

  @PostMapping
  public ResponseEntity<Certificate> createCertificate(@RequestBody Certificate certificate) {
    Certificate createdCertificate = certificateService.create(certificate);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCertificate);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Certificate> updateCertificatePut(
      @PathVariable long id, @RequestBody Certificate certificate) {
    certificate.setId(id);
    Optional<Certificate> updatedCertificate = certificateService.updatePut(certificate);
    return updatedCertificate
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Certificate> updateCertificatePatch(
      @PathVariable long id, @RequestBody Certificate certificate) {
    certificate.setId(id);
    Optional<Certificate> updatedCertificate = certificateService.updatePatch(certificate);
    return updatedCertificate
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable long id) {
    certificateService.delete(id);
  }
}
