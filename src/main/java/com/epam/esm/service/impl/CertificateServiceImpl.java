package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class CertificateServiceImpl implements CertificateService {

  private final CertificateDao certificateDao;
  private final TagDao tagDao;

  public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
    this.certificateDao = certificateDao;
    this.tagDao = tagDao;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Certificate create(Certificate certificate) {
    Certificate createdCertificate = certificateDao.create(certificate);
    List<Tag> tags = certificate.getTags();
    tags.forEach(
        tag -> {
          Optional<Tag> existedTag = tagDao.read(tag);
          long tagId =
              (existedTag.isEmpty()) ? tagDao.create(tag).getId() : existedTag.get().getId();
          certificateDao.addTag(tagId, createdCertificate.getId());
        });
    return createdCertificate;
  }

  @Override
  public Certificate read(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    certificate.ifPresent(value -> value.setTags(certificateDao.readTags(id)));
    return certificate.orElse(null);
  }

  @Override
  public List<Certificate> readAll() {
    List<Certificate> certificates = certificateDao.readAll();
    certificates.forEach(
        certificate -> certificate.setTags(certificateDao.readTags(certificate.getId())));
    return certificates;
  }

  @Override
  public Certificate update(long id, Certificate certificate) {
    // TODO check empty fields & tags
    certificate.setId(id);
    List<Tag> tags = certificate.getTags();
    tags.forEach(
        tag -> {
          Optional<Tag> existedTag = tagDao.read(tag);
          long tagId =
              (existedTag.isEmpty()) ? tagDao.create(tag).getId() : existedTag.get().getId();
          certificateDao.addTag(tagId, id);
        });
    // TODO return from dao mb
    certificateDao.update(certificate);
    return certificate;
  }

  @Override
  public void delete(long id) {
    // TODO delete tag dependency
    certificateDao.delete(id);
  }
}
