package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.GetParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.ReflectionUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class CertificateServiceImpl implements CertificateService {

  public static final int ONE_UPDATED_ROW = 1;
  private final CertificateDao certificateDao;
  private final TagDao tagDao;
  private final ReflectionUtil util; // TODO remove with old updatePatch

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
        actualCertificate -> actualCertificate.setTags(certificateDao.readCertificateTags(id)));
    return certificate;
  }

  @Override
  public List<Certificate> readAll(GetParameter parameter) {
    List<Certificate> certificates = certificateDao.readAll(parameter);
    for (Certificate certificate : certificates) {
      certificate.setTags(certificateDao.readCertificateTags(certificate.getId()));
    }
    return certificates;
  }

  @Override // TODO what is going on: insert to db only not-null fields->put updated certificate back
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Optional<Certificate> updatePatch(Certificate certificate) {
    int numberOfUpdatedRows = certificateDao.updatePatch(certificate);
    if (numberOfUpdatedRows == ONE_UPDATED_ROW) {
      if (certificate.getTags() != null) {
        certificateDao.deleteCertificateTagsByCertificateId(certificate.getId());
        addTagsToDb(certificate);
      } else {
        List<Tag> tags = certificateDao.readCertificateTags(certificate.getId());
        certificate.setTags(tags);
      }
      return read(certificate.getId());
    }
    return Optional.empty();
  }

  //  @Override TODO what is going on: read from db->fill nullable->put back to db
  //  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  //  public Optional<Certificate> updatePatch(Certificate certificate) {
  //    Optional<Certificate> existedCertificate = certificateDao.read(certificate.getId());
  //    if (existedCertificate.isPresent()) {
  //      util.fillNullFields(certificate, existedCertificate.get());
  //      certificateDao.update(certificate);
  //      if (certificate.getTags() != null) {
  //        certificateDao.deleteCertificateTagsByCertificateId(certificate.getId());
  //        addTagsToDb(certificate);
  //      } else {
  //        List<Tag> tags = certificateDao.readCertificateTags(certificate.getId());
  //        certificate.setTags(tags);
  //      }
  //      return Optional.of(certificate);
  //    }
  //    return Optional.empty();
  //  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public Optional<Certificate> updatePut(Certificate certificate) {
    int numberOfUpdatedRows = certificateDao.update(certificate);
    if (numberOfUpdatedRows == ONE_UPDATED_ROW) {
      certificateDao.deleteCertificateTagsByCertificateId(certificate.getId());
      addTagsToDb(certificate);
      return Optional.of(certificate);
    }
    return Optional.empty();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
  public void delete(long id) {
    certificateDao.deleteCertificateTagsByCertificateId(id);
    certificateDao.delete(id);
  }

  void addTagsToDb(Certificate certificate) {
    List<Tag> tags = certificate.getTags();
    if (tags != null) {
      for (Tag tag : tags) {
        Optional<Tag> existedTag = tagDao.read(tag.getName());
        long tagId = existedTag.map(Tag::getId).orElseGet(() -> tagDao.create(tag).getId());
        certificateDao.addTag(tagId, certificate.getId());
      }
    }
  }
}
