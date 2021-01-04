package com.epam.esm.service;

import com.epam.esm.entity.CertificateDto;

import java.util.List;

public interface CertificateService {

  void create(CertificateDto certificate);

  CertificateDto read(long id);

  List<CertificateDto> readAll();

  void update(long id, CertificateDto certificate);

  void delete(long id);
}
