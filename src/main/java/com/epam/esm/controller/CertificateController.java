package com.epam.esm.controller;

import com.epam.esm.entity.CertificateDto;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

  @Autowired CertificateService certificateService;

  @GetMapping("/{id}")
  public CertificateDto getCertificate(@PathVariable long id) {
    return certificateService.read(id);
  }

  @GetMapping
  public List<CertificateDto> getCertificates() {
    return certificateService.readAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createCertificate(@RequestBody CertificateDto certificate) {
    certificateService.create(certificate);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateCertificate(@PathVariable long id, @RequestBody CertificateDto certificate) {
    certificateService.update(id, certificate);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCertificate(@PathVariable long id) {
    certificateService.delete(id);
  }
}
