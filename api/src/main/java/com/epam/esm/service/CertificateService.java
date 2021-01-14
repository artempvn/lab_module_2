package com.epam.esm.service;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.GetParameter;

import java.util.List;
import java.util.Optional;

public interface CertificateService {

  Certificate create(Certificate certificate);

  Optional<Certificate> read(long id);

  List<Certificate> readAll(GetParameter parameter);

  Optional<Certificate> updatePut(Certificate certificate);

  Optional<Certificate> updatePatch(Certificate certificate);

  void delete(long id);
}
