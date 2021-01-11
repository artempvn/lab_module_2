package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.ReflectionUtil;
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
  private final ReflectionUtil util;

  public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao, ReflectionUtil util) {
    this.certificateDao = certificateDao;
    this.tagDao = tagDao;
    this.util = util;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Certificate create(Certificate certificate) {
    Certificate createdCertificate = certificateDao.create(certificate);
    addTagsToDb(createdCertificate);
    return createdCertificate;
  }

  @Override
  public Optional<Certificate> read(long id) {
    Optional<Certificate> certificate = certificateDao.read(id);
    certificate.ifPresent(
        actualCertificate -> actualCertificate.setTags(certificateDao.readBondingTags(id)));
    return certificate;
  }

  @Override
  public List<Certificate> readAll() {
    List<Certificate> certificates = certificateDao.readAll();
    for (Certificate certificate : certificates) {
      certificate.setTags(certificateDao.readBondingTags(certificate.getId()));
    }
    return certificates;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Optional<Certificate> update(long id, Certificate certificate) {
    Optional<Certificate> existedCertificate = certificateDao.read(id);
    if (existedCertificate.isPresent()) {
      util.fillNullFields(certificate, existedCertificate.get());
      certificateDao.update(certificate);
      if (certificate.getTags() != null) {
        certificateDao.deleteBondingTagsByCertificateId(id);
        addTagsToDb(certificate);
      } else {
        List<Tag> tags = certificateDao.readBondingTags(id);
        certificate.setTags(tags);
      }
    }
    return (existedCertificate.isPresent()) ? (Optional.of(certificate)) : (Optional.empty());
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void delete(long id) {
    certificateDao.deleteBondingTagsByCertificateId(id);
    certificateDao.delete(id);
  }

  private void addTagsToDb(Certificate certificate) {
    List<Tag> tags = certificate.getTags();
    if (tags != null) {
      for (Tag tag : tags) {
        Optional<Tag> existedTag = tagDao.read(tag);
        long tagId = existedTag.map(Tag::getId).orElseGet(() -> tagDao.create(tag).getId());
        certificateDao.addTag(tagId, certificate.getId());
      }
    }
  }
}
