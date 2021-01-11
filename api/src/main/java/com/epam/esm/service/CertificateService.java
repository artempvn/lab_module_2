package com.epam.esm.service;

import com.epam.esm.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateService {

  Certificate create(Certificate certificate);

  Optional<Certificate> read(long id);

  List<Certificate> readAll();

  Optional<Certificate> update(long id, Certificate certificate);

  void delete(long id);
}
