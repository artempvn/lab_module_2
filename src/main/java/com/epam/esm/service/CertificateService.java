package com.epam.esm.service;

import com.epam.esm.entity.Certificate;

import java.util.List;

public interface CertificateService {

  Certificate create(Certificate certificate);

  Certificate read(long id);

  List<Certificate> readAll();

  Certificate update(long id, Certificate certificate);

  void delete(long id);
}
